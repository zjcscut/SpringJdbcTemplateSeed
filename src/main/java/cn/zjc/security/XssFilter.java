package cn.zjc.security;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author zjc
 * @version 2016/10/17 23:16
 * @description Xss过滤器, 可处理multipart请求
 */
public class XssFilter extends OncePerRequestFilter {

	private MultipartResolver multipartResolver;
	private String encoding;

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public XssFilter() {
	}

	@Override
	protected void initFilterBean() throws ServletException {
		XssCommonsMultipartResolver resolver = new XssCommonsMultipartResolver();
		resolver.setDefaultEncoding(StandardCharsets.UTF_8.name());
		resolver.setMaxUploadSize(4096000);
		resolver.setServletContext(getServletContext());
		multipartResolver = resolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
									FilterChain filterChain) throws ServletException, IOException {
		if (!isBlank(encoding)) {
			httpServletRequest.setCharacterEncoding(encoding);
			httpServletResponse.setCharacterEncoding(encoding);
		} else {
			httpServletRequest.setCharacterEncoding(StandardCharsets.UTF_8.name());
			httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
		}
		boolean multipartRequestParsed = false;
		HttpServletRequest request = new XssHttpServletRequestWrapper(httpServletRequest);
		if (multipartResolver.isMultipart(request)) {
			request = multipartResolver.resolveMultipart(request);
			multipartRequestParsed = true;
		}
		try {
			filterChain.doFilter(request, httpServletResponse);
		} finally {
			if (multipartRequestParsed) {
				multipartResolver.cleanupMultipart(WebUtils.getNativeRequest(
						httpServletRequest, MultipartHttpServletRequest.class
				));
			}
		}
	}

	private boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}
}
