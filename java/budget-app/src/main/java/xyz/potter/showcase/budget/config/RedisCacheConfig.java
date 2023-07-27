package xyz.potter.showcase.budget.config;

import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import com.google.protobuf.InvalidProtocolBufferException;
import xyz.potter.showcase.budget.proto.Budget;

@Profile("!local")
@Configuration
@EnableCaching
public class RedisCacheConfig {

  @Bean
  public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheConfiguration budgetCacheConfiguration =
        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(1))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new RedisSerializer<Budget>() {

              @Override
              public Budget deserialize(byte[] b) throws SerializationException {
                try {
                  return Budget.parseFrom(b);
                } catch (InvalidProtocolBufferException e) {
                  throw new SerializationException(e.toString(), e);
                }
              }

              @Override
              public byte[] serialize(Budget budget) throws SerializationException {
                return budget.toByteArray();
              }

            }));



    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
        .withCacheConfiguration("budget", budgetCacheConfiguration).build();
  }

}
