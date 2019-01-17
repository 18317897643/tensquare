package com.tensquare.spit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

@SpringBootApplication
public class SpitApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpitApplication.class,args);
    }
    @Bean
    public IdWorker getIdWorker(){
        return new IdWorker();
    }
    /**
     * spring data mongodb在插入和修改数据时会在mongo中添加_class字段，保存类名。
     * 这样做的原因在于将mongodb中的文档对象转化为java对象时，可以将其转化为具体的子类  所以不需要将_class属性移除
     *  下边的代码的作用就是_class属性移除
     *  具体代码为：mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
     */
//    @Bean
//    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, BeanFactory beanFactory) {
//        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
//        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
//        try {
//            mappingConverter.setCustomConversions(beanFactory.getBean(CustomConversions.class));
//        } catch (NoSuchBeanDefinitionException ignore) {
//        }
//        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
//
//        return mappingConverter;
//    }
}
