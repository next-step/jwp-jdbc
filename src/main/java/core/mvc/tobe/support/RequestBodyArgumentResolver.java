/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.tobe.MethodParameter;
import core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {

	@Override
	public boolean supports(MethodParameter methodParameter) {
		return supportAnnotation(methodParameter, RequestBody.class);
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
		try {
			String jsonBody = IOUtils.toString(request.getReader());
			log.debug("RequestBodyArgumentResolver - jsonBody: {}", jsonBody);
			return StringUtils.fromJson(jsonBody, methodParameter.getType());
		}
		catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
}
