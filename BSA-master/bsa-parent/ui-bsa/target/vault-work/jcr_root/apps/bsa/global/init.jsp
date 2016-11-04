
<%@include file="/apps/bsa/global/global.jsp"
%>
<%@page import="com.day.cq.wcm.api.WCMMode, com.day.cq.wcm.api.AuthoringUIMode" %>
<%
if (WCMMode.fromRequest(request) != WCMMode.DISABLED) {
    String dlgPath = null;
    if (editContext != null && editContext.getComponent() != null) {
        dlgPath = editContext.getComponent().getDialogPath();
    }
    if (AuthoringUIMode.fromRequest(slingRequest) == AuthoringUIMode.TOUCH) {
            %>
<cq:includeClientLib categories="cq.authoring.page" />
<%
        } else if (AuthoringUIMode.fromRequest(slingRequest) == AuthoringUIMode.CLASSIC) {
    %>
<cq:includeClientLib categories="cq.wcm.edit" />
<script type="text/javascript">
        (function() {
            var cfg = <%
                try {
                    sling.getService(com.day.cq.wcm.undo.UndoConfigService.class)
                            .writeClientConfig(out);
                } catch (Exception e) {
                    // ignore
                }
            %>;
            // explicitly set page path, as under some circumstances, CQ.WCM.getPagePath()
            // may yield unwanted results at this point
            cfg.pagePath = "<%= currentPage.getPath() %>";

            if (CQClientLibraryManager.channelCB() != "touch") {
                var isEditMode = <%= WCMMode.fromRequest(request) == WCMMode.EDIT ? "true" : "false" %>;
                if (!isEditMode) {
                    cfg.enabled = false;
                }
                CQ.undo.UndoManager.initialize(cfg);
                CQ.Ext.onReady(function() {
                    CQ.undo.UndoManager.detectCachedPage(<%= System.currentTimeMillis() %>);
                });
            }
        })();
        CQ.WCM.launchSidekick("<%= currentPage.getPath() %>", {
            propsDialog: "<%= dlgPath == null ? "" : dlgPath %>",
            locked: <%= currentPage.isLocked() %>,
            previewReload: "true"
        });
    </script>
<% 
    }
} %>
