package io.leopard.data.env;

import java.util.Properties;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;

public class LeopardPropertyPlaceholderConfigurer extends org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {

	private ResolvePlaceholderLei resolvePlaceholderLei = new ResolvePlaceholderLeiImpl();

	public LeopardPropertyPlaceholderConfigurer() {
		super.setIgnoreResourceNotFound(true);
		super.setOrder(999);
		super.setIgnoreUnresolvablePlaceholders(true);
		super.setSystemPropertiesMode(SYSTEM_PROPERTIES_MODE_FALLBACK);

	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		super.setBeanFactory(beanFactory);
		PropertyDecoder propertyDecoder;
		try {
			propertyDecoder = beanFactory.getBean(PropertyDecoder.class);
		}
		catch (NoUniqueBeanDefinitionException e) {
			logger.error(e.getMessage(), e);
			propertyDecoder = new PropertyDecoderImpl();
		}
		catch (NoSuchBeanDefinitionException e) {
			propertyDecoder = new PropertyDecoderImpl();
		}
		String env = EnvUtil.getEnv();
		PropertyPlaceholderLeiImpl propertyPlaceholderLeiImpl = new PropertyPlaceholderLeiImpl();
		propertyPlaceholderLeiImpl.setPropertyDecoder(propertyDecoder);
		super.setLocations(propertyPlaceholderLeiImpl.getResources(env));
	}

	@Override
	// 执行顺序:2
	protected String resolvePlaceholder(String placeholder, Properties props) {
		String value = super.resolvePlaceholder(placeholder, props);
		if (value == null) {
			value = resolvePlaceholderLei.resolvePlaceholder(placeholder, props);
		}
		return value;
	}

}
