```java
@ParamRequired({
    "u:String:会员id",
    "appId:String:应用id",
    "comid:String:管理公司id",
    "shouldpay:String:应付金额",
    "applyprice:String:申请扣费的金额",
    "orderid:String:卡类型ID",
    "casetype:String:支付场景类型 1-全部余额支付2,-全部第三方支付 3-部分余额支付",
})
@ParamImplied({
    "channel: String:　充值渠道-必填 1支付宝，2财付通，3微信，4微信公众号，5银行，99直接充值",
    "sub_type:String:channel子类型:channel=5",
    "ClientType:String:客户端类型",
})
@Output({
    "state: Int: 返回状态(1-成功,其他值-失败)",
    "message: String: 返回提示"
})
@FrequencyCheck({"u,orderid:1"})
@Desc("支付停车场订单")
public ParaMap payPloOrder(ParaMap inMap) throws Exception
    ParaMap validate = ParamUtils.validate(inMap,new AppAccess());
    if (validate.getInt(STATE)==S2) {
        return validate;
    }
    //===///
}

public static ParaMap validate(ParaMap inMap,Dealer...filters) throws Exception{}

public interface Dealer
{
    ParaMap doDeal(ParaMap inMap,ParaMap outMap,DealerChain dealChain);
}


public class DealerChain implements Dealer {
        private List<Dealer> dealers = new ArrayList<>();
        private int account = 0;

        public void addDealer(Dealer filter){
            dealers.add(filter);
        }

        @Override
        public ParaMap doDeal(ParaMap inMap,ParaMap outMap,DealerChain dealChain)
        {
            if (dealers.size()==account){
                return outMap;            
            }
            outMap = dealers.get(account++).doDeal(inMap,outMap, dealChain);
            if (outMap.getInt(STATE)==2){
                ParaMap out = new ParaMap();
                out.put(STATE, 2);
                out.put(MESSAGE,outMap.getString(MESSAGE) );
                return out;
            }
            return outMap;
        }

        @Override
        public Logging getLog()
        {
            return Logging.getLogging(this.getClass().getCanonicalName());
        }
}
```





```java
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidItems
{
    public ValidItem[] value() default {};
}




@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ValidItems.class)
public @interface ValidItem
{
    /**
     * 参数名称
     */
    public String fieldName();
    /**
     * 验证类型<p>
     * 默认：{@link com.valid.ValidType.NOT_EMPTY}
     */
    public ValidType validType() default ValidType.NOT_EMPTY;
    /**
     * 错误信息
     */
    public String message();
    /**
     * 验证组
     * 默认：{@link com.valid.Validator.Default}
     */
    public Class<?>[] groups() default {DefaultGroup.class};
}


public enum ValidType
{
    /**
     * 不为空
     */
    NOT_NULL {
        @Override
        protected boolean validRule(Object value)
        {
            if(value == null){
                return false;
            }
            return true;
        }
    },
    /**
     * 不为空（包括空值）<p>
     * 用于字符串
     */
    NOT_EMPTY {
        @Override
        protected boolean validRule(Object value)
        {
            if(value == null || StringUtils.isEmpty(value.toString())){
                return false;
            }
            return true;
        }
    };
    
    /**
     * 验证
     * @param object 验证对象
     * @param fieldName 参数名称
     * @return 验证结果
     * @throws Exception
     */
    public boolean valid(Object object, String fieldName) throws Exception{
        Object value = ReflectUtils.getValue(fieldName, object);
        return validRule(value);
        
    }
    
    protected abstract boolean validRule(Object value);
}
，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，
public class Validator {
    
    /**
     * 错误消息
     */
    public static ThreadLocal<String> message = new ThreadLocal<String>();
    private static final String MODULEFLAG = "#module#";
    private static final String SERVICEFLAG = "#service#";
    private static final String SERVICEMODEL = "com."+MODULEFLAG+".service."+SERVICEFLAG+"Service";
    private static final Class<?>[] DEFAULTGROUPS = new Class<?>[]{DefaultGroup.class};
    
    /**
     * 验证参数<p>
     * 仅支持对外接口做参数验证
     * @param object 参数对象
     * @return 验证结果
     * @throws Exception
     * @author 罗尚林
     */
    public static boolean valid(Object object, Class<?>... groups) throws Exception{
        message.remove();
        ValidItem[] items = null;
        groups = groups.length == 0 ? DEFAULTGROUPS : groups;
        try {
            String module = ReflectUtils.getValue("module", object).toString();
            String service = ReflectUtils.getValue("service", object).toString();
            String method = ReflectUtils.getValue("method", object).toString();
            String className = SERVICEMODEL.replaceAll(MODULEFLAG, module).replaceAll(SERVICEFLAG, service);
            items = Class.forName(className).getMethod(method,ParaMap.class).getAnnotationsByType(ValidItem.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取接口报错");
        }
        for (ValidItem validItem : items)
        {
            if(!contains(groups, validItem.groups())){
                continue;
            }
            if(!validItem.validType().valid(object, validItem.fieldName())){
                message.set(validItem.message());
                return false;
            }
        }
        return true;
    }
    
    /**
     * 是否包含数组中任意元素
     * @param groups 数组1
     * @param group 数组2
     * @return
     * @author 罗尚林
     */
    private static boolean contains(Class<?>[] groups, Class<?>[] group){
        for (Class<?> class1 : groups)
        {
            if(contain(group, class1)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * 是否包含元素
     * @param groups 数组
     * @param group 元素
     * @return
     * @author 罗尚林
     */
    private static boolean contain(Class<?>[] groups, Class<?> group){
        for (Class<?> class1 : groups)
        {
            if(class1.equals(group)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * 默认验证组
     * @author 罗尚林
     */
    public static interface DefaultGroup{
        
    }
    
    /**
     * ID组
     * @author 罗尚林
     *
     */
    public static interface IDGroup{
        
    }
    
    /**
     * 添加组
     * @author 罗尚林
     *
     */
    public static interface InsertGroup{
        
    }
    
    /**
     * 更新组
     * @author 罗尚林
     *
     */
    public static interface UpdateGroup{
        
    }
    
}

```

