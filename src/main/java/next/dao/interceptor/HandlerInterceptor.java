package next.dao.interceptor;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerInterceptor {

    /**
     * 컨트롤러 호출 전
     * 컨트롤러 실행 이전에 처리해야할 작업이 있는경우, 혹은 가공해야할 경우
     * true 리턴 시 컨트롤러 실행
     * @param request
     * @param response
     * @param handler
     * @return
     */
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    };

    /**
     * 핸들러의 실행이 완료 되었지만 view 생성이전
     * Model 객체 조작가능
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     */
    void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView);

    /**
     * 최종 결과를 생성하는 일을 포함한 모든 작업이 완료 된 후 실행
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception;
}
