package dio.springtoken.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.config")
public class  SecurityConfig {

	public String PREFIX; 
	public String KEY;
	public long EXPIRATION;
	
	public void setEXPIRATION(long eXPIRATION) {
		EXPIRATION = eXPIRATION;
	}
	public void setKEY(String kEY) {
		KEY = kEY;
	}
	public void setPREFIX(String pREFIX) {
		PREFIX = pREFIX;
	}
}




