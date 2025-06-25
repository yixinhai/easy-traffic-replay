package com.xh.easy.trafficreplay.plugin.idea.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.xh.easy.trafficreplay.plugin.idea.ui.JsonInputWindow;
import org.jetbrains.annotations.NotNull;

/**
 * 注入方法的右键菜单动作
 * 
 * @author yixinhai
 */
public class InjectMethodAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        if (editor == null || psiFile == null) {
            return;
        }

        // 获取当前光标位置的方法
        PsiElement elementAtCaret = psiFile.findElementAt(editor.getCaretModel()
            .getOffset());
        PsiMethod method = PsiTreeUtil.getParentOfType(elementAtCaret, PsiMethod.class);

        if (method == null) {
            return;
        }

        // 获取方法的完整签名
        String methodSignature = getMethodSignature(method);

        // 显示JSON输入窗口
        showJsonInputWindow(project, methodSignature);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        // 只在Java文件中且光标在方法内时显示此动作
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        boolean visible = false;
        if (editor != null && psiFile instanceof PsiJavaFile) {
            PsiElement elementAtCaret = psiFile.findElementAt(editor.getCaretModel()
                .getOffset());
            PsiMethod method = PsiTreeUtil.getParentOfType(elementAtCaret, PsiMethod.class);
            visible = method != null;
        }

        e.getPresentation()
            .setVisible(visible);
    }

    /**
     * 获取方法的完整签名
     */
    private String getMethodSignature(PsiMethod method) {
        PsiClass containingClass = method.getContainingClass();
        if (containingClass == null) {
            return "";
        }

        String className = containingClass.getQualifiedName();
        String methodName = method.getName();

        StringBuilder signature = new StringBuilder();
        signature.append(className)
            .append("#")
            .append(methodName)
            .append("(");

        PsiParameter[] parameters = method.getParameterList()
            .getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) {
                signature.append(", ");
            }
            PsiType type = parameters[i].getType();
            signature.append(type.getCanonicalText());
        }

        signature.append(")");
        return signature.toString();
    }

    /**
     * 显示JSON输入窗口
     */
    private void showJsonInputWindow(Project project, String methodSignature) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow("Traffic Replay");

        if (toolWindow == null) {
            // 如果工具窗口不存在，创建一个
            toolWindow = toolWindowManager.registerToolWindow("Traffic Replay", true,
                com.intellij.openapi.wm.ToolWindowAnchor.RIGHT);
        }

        // 创建或更新JSON输入窗口内容
        JsonInputWindow jsonInputWindow = new JsonInputWindow(project, methodSignature);
        toolWindow.getContentManager()
            .removeAllContents(true);
        toolWindow.getContentManager()
            .addContent(toolWindow.getContentManager()
                .getFactory()
                .createContent(jsonInputWindow.getPanel(), "JSON Input", false));

        // 显示工具窗口
        toolWindow.activate(null);
    }
}