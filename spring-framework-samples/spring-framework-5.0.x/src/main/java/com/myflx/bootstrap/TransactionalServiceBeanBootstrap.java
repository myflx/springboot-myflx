package com.myflx.bootstrap;

import com.myflx.bean.TransactionalServiceBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.SimpleTransactionStatus;

import java.util.Map;

@Configuration
@ComponentScan(basePackageClasses = TransactionalServiceBean.class)
@EnableTransactionManagement
public class TransactionalServiceBeanBootstrap {
    public static void main(String[] args) {
        ConfigurableApplicationContext context =
            new AnnotationConfigApplicationContext(TransactionalServiceBeanBootstrap.class);
        Map<String, TransactionalServiceBean> beansOfType = context.getBeansOfType(TransactionalServiceBean.class);
        beansOfType.forEach((name,bean)->{
            System.out.printf("Bean 名称 ：%s ,对象 ： %s \n",name,bean);
            bean.save();
        });
        Object txManager = context.getBean("txManager");
        Object txManager2 = context.getBean("txManager2");
        System.out.println(txManager);
        System.out.println((txManager instanceof PlatformTransactionManager));
        System.out.println(txManager2);
        System.out.println((txManager2 instanceof PlatformTransactionManager));
    }

    @Bean("txManager")
    public PlatformTransactionManager txManager(){
        return new PlatformTransactionManager() {
            @Override
            public TransactionStatus getTransaction(TransactionDefinition transactionDefinition) throws TransactionException {
                return new SimpleTransactionStatus();
            }

            @Override
            public void commit(TransactionStatus transactionStatus) throws TransactionException {
                System.out.println("事务提交。。");
            }

            @Override
            public void rollback(TransactionStatus transactionStatus) throws TransactionException {
                System.out.println("事务回滚。。");
            }
        };
    }

    @Bean("txManager2")
    public PlatformTransactionManager txManager2(){
        return new PlatformTransactionManager() {
            @Override
            public TransactionStatus getTransaction(TransactionDefinition transactionDefinition) throws TransactionException {
                return new SimpleTransactionStatus();
            }

            @Override
            public void commit(TransactionStatus transactionStatus) throws TransactionException {
                System.out.println("txManager2：事务提交。。");
            }

            @Override
            public void rollback(TransactionStatus transactionStatus) throws TransactionException {
                System.out.println("txManager2：事务回滚。。");
            }
        };
    }
}
