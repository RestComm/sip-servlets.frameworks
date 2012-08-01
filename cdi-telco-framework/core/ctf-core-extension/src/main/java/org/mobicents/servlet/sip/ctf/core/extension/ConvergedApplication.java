package org.mobicents.servlet.sip.ctf.core.extension;

import javax.servlet.ServletContext;

public class ConvergedApplication {

    private final String name;
    private final String contextPath;
    private final String serverInfo;
    private final long startTime;

    public ConvergedApplication(ServletContext ctx) {
        this(ctx.getServletContextName(), ctx.getContextPath(), ctx.getServerInfo());
    }

    public ConvergedApplication(String name, String contextPath, String serverInfo) {
        this.name = name;
        this.contextPath = contextPath;
        this.serverInfo = serverInfo;
        this.startTime = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getServerInfo() {
        return serverInfo;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getRunningTime() {
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contextPath == null) ? 0 : contextPath.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((serverInfo == null) ? 0 : serverInfo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ConvergedApplication)) {
            return false;
        }

        ConvergedApplication other = (ConvergedApplication) obj;
        if (contextPath == null) {
            if (other.contextPath != null) {
                return false;
            }
        } else if (!contextPath.equals(other.contextPath)) {
            return false;
        }

        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }

        if (serverInfo == null) {
            if (other.serverInfo != null) {
                return false;
            }
        } else if (!serverInfo.equals(other.serverInfo)) {
            return false;
        }

        return true;
    }
	
}
