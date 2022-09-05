package core.mvc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.interceptor.Interceptor;

public class HandlerInterceptorRegistry {
	private final List<Interceptor> interceptors = new ArrayList<>();

	public void addInterceptor(Interceptor interceptor) {
		interceptors.add(interceptor);
	}

	public void preHandle(HttpServletRequest request, HttpServletResponse response) {
		interceptors.forEach(interceptor -> interceptor.preHandle(request, response));
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response) {
		interceptors.forEach(interceptor -> interceptor.postHandle(request, response));
	}
}
