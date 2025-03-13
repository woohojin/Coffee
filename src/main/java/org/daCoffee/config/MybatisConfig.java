package org.daCoffee.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@MapperScan(basePackages = "org.daCoffee.service")
public class MybatisConfig {
  private static final Logger LOGGER = LoggerFactory.getLogger(MybatisConfig.class);

  @Bean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

    // mybatis가 DB를 불러올 수 있도록 함
    factoryBean.setDataSource(dataSource);
    // 지정한 하위 디렉토리의 모든 클래스들은 별칭으로 등록 ex) EncryptionTypeHandler.java = encryptionTypeHandler
    factoryBean.setTypeAliasesPackage("org.daCoffee.handler,org.daCoffee.model");

    // mybatis 하위에 있는 모든 mapper를 불러옴
    Resource[] mappers = new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/*.xml");
    LOGGER.info("Loading mapper files : {}", Arrays.toString(mappers));
    factoryBean.setMapperLocations(mappers);

    SqlSessionFactory factory = factoryBean.getObject();
    LOGGER.info("SqlSessionFactory created: {}", factory);
    return factory;
  }

  @Bean
  public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }
}