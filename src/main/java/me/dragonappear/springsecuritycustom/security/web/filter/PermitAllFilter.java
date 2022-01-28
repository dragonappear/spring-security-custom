package me.dragonappear.springsecuritycustom.security.web.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PermitAllFilter extends FilterSecurityInterceptor {

    private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied";
    private boolean observeOncePerRequest = true;

    List<RequestMatcher> permitRequestList = new ArrayList<>();

    public PermitAllFilter(List<String> permitList) {
        for (String resource : permitList) {
            permitRequestList.add(new AntPathRequestMatcher(resource));
        }
    }


    @Override
    protected InterceptorStatusToken beforeInvocation(Object object) {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        boolean permitAll = false;
        for (RequestMatcher matcher : permitRequestList) {
            if (matcher.matches(request)) {
                permitAll = true;
                break;
            }
        }
        if (permitAll) {
            return null;
        }
        return super.beforeInvocation(object);
    }


    public void invoke(FilterInvocation filterInvocation) throws IOException, ServletException {
        if (isApplied(filterInvocation) && this.observeOncePerRequest) {
            filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
            return;
        }
        if (filterInvocation.getRequest() != null && this.observeOncePerRequest) {
            filterInvocation.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
        }

        InterceptorStatusToken token = beforeInvocation(filterInvocation);
        try {
            filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
        }
        finally {
            super.finallyInvocation(token);
        }
        super.afterInvocation(token, null);
    }


    private boolean isApplied(FilterInvocation filterInvocation) {
        return (filterInvocation.getRequest() != null)
                && (filterInvocation.getRequest().getAttribute(FILTER_APPLIED) != null);
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

}
