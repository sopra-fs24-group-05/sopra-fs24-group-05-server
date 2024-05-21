package ch.uzh.ifi.hase.soprafs24.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
  // 自动注册使用@ServerEndpoint注解申明的websocket endpoint
  @Bean
//  @Profile("!test") // 仅在非测试环境中启用
  public ServerEndpointExporter serverEndpointExporter() {
      return new ServerEndpointExporter();
  }
}
