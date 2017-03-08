package org.talayos;

import org.apache.catalina.Context;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class MultiLanguageEditorApplication {
	public static void main(String[] args) {
		SpringApplication.run(MultiLanguageEditorApplication.class, args);
	}

	@Bean
	public TomcatEmbeddedServletContainerFactory tomcatEmbedded() {

		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();

		tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
			if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
				//-1 means unlimited
				((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
			}
		});

		TomcatContextCustomizer contextCustomizer = new TomcatContextCustomizer() {
			@Override
			public void customize(Context context) {
				context.addWelcomeFile("/index.html");
			}
		};
		tomcat.addContextCustomizers(contextCustomizer);
		return tomcat;

	}
}
