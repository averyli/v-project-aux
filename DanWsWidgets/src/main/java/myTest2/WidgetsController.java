package myTest2;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import annotations.Widget;
import dynamic.WPack;

@Controller
public class WidgetsController {

	private @Autowired AutowireCapableBeanFactory beanFactory;
	
	@Autowired
	private Logger logger;
	
	private List<WPack> wpacks;
	
	/**
	 * Method to instantiate a new widget
	 *
	 * @param widgetId
	 * @return
	 */
	@MessageMapping("/widgetsController")
	@SendTo("/topic/activate")
	public String activate(){
		
		wpacks = new ArrayList<>();
		
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Widget.class));
		
		StringBuffer classes = new StringBuffer();
		for (BeanDefinition bd : scanner.findCandidateComponents("")){
			
			try {
				Class<?> cs = Class.forName(bd.getBeanClassName());
				Widget w = cs.getAnnotation(Widget.class);
				String name = w.name();
				
				WPack wpack = new WPack();
				
				try {
					wpack.obj = cs.newInstance();
					beanFactory.autowireBean(wpack.obj);
				} catch (InstantiationException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(cs.getDeclaredMethods()));
				for (final Method method : allMethods) {
		            if (method.isAnnotationPresent(MessageMapping.class)) {
		            	wpack.incoming = method;
		            }
				}
				
				wpacks.add(wpack);
				logger.info("added wpack");
				
				classes.append("[").append(name).append(": ").append(wpack.incomingId).append(", ");
				
				cs.getAnnotation(MessageMapping.class);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return classes.toString();
	}
	
	@MessageMapping("/incoming/params/{widgetId}")
	public void catchMessageForWidgetParametrizied(@DestinationVariable String widgetId, String object) {
		logger.info("got message with parameter");
		logger.info(object.toString());
		logger.info(object.getClass().getName());
		wpacks.stream().filter(wp->wp.incomingId.equals(widgetId)).forEach(wp -> {
			try {
				ObjectMapper mapper = new ObjectMapper();
				
				Class<?> paramClass = wp.incoming.getParameterTypes()[0];
				logger.info("parameter class for widget method is "+paramClass.getName());
				
				wp.incoming.invoke(wp.obj, mapper.readValue(object, paramClass));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	@MessageMapping("/incoming/empty/{widgetId}")
	public void catchMessageForWidgetNoParams(@DestinationVariable String widgetId) {
		logger.info("got message without parameters");
		wpacks.stream().filter(wp->wp.incomingId.equals(widgetId)).forEach(wp -> {
			try {
				wp.incoming.invoke(wp.obj);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}
