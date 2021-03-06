package com.google.jstestdriver.idea.assertFramework.support;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.AsyncResult;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.util.FileContentUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

class AdapterFix implements LocalQuickFix {

  private final AbstractAddAdapterSupportInspection myAssertFrameworkAdapterSupportProvider;

  AdapterFix(AbstractAddAdapterSupportInspection assertFrameworkAddAdapterSupportInspection) {
    myAssertFrameworkAdapterSupportProvider = assertFrameworkAddAdapterSupportInspection;
  }

  @NotNull
  //@Override
  public String getText() {
    String assertFrameworkName = myAssertFrameworkAdapterSupportProvider.getAssertFrameworkName();
    return "Add " + assertFrameworkName + " adapter support for JsTestDriver";
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return getText();
  }

  public void invoke(@NotNull final Project project, Editor editor, final PsiFile file) throws IncorrectOperationException {
    ApplicationManager.getApplication().invokeLater(new Runnable() {
      @Override
      public void run() {
        AddAdapterSupportDialog dialog = new AddAdapterSupportDialog(
            project,
            myAssertFrameworkAdapterSupportProvider.getAssertFrameworkName(),
            myAssertFrameworkAdapterSupportProvider.getAdapterSourceFiles()
        );
        final AsyncResult<Boolean> result = dialog.showAndGetOk();
        final VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile != null) {
          result.doWhenDone(new AsyncResult.Handler<Boolean>() {
            @Override
            public void run(Boolean ok) {
              if (ok) {
                FileContentUtil.reparseFiles(project, Arrays.<VirtualFile>asList(virtualFile), true);
              }
            }
          });
        }
      }
    });
  }

//  LocalQuickFix methods implementation

  @NotNull
  @Override
  public String getName() {
    return getText();
  }

  @Override
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
    final Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
    invoke(project, editor, descriptor.getPsiElement().getContainingFile());
  }

}
