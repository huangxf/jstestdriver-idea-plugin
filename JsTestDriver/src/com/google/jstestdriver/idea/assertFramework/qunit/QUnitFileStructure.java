package com.google.jstestdriver.idea.assertFramework.qunit;

import com.google.common.collect.Lists;
import com.google.inject.internal.Maps;
import com.google.jstestdriver.idea.assertFramework.AbstractTestFileStructure;
import com.google.jstestdriver.idea.assertFramework.JstdRunElement;
import com.google.jstestdriver.idea.util.CastUtils;
import com.google.jstestdriver.idea.util.JsPsiUtils;
import com.intellij.lang.javascript.psi.JSFile;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class QUnitFileStructure extends AbstractTestFileStructure {

  private final List<QUnitModuleStructure> myNonDefaultModuleStructures = Lists.newArrayList();
  private final Map<String, QUnitModuleStructure> myNonDefaultModuleStructureByNameMap = Maps.newHashMap();
  private final DefaultQUnitModuleStructure myDefaultModuleStructure = new DefaultQUnitModuleStructure(this);

  public QUnitFileStructure(@NotNull JSFile jsFile) {
    super(jsFile);
  }

  public int getAllModuleCount() {
    return myNonDefaultModuleStructures.size() + 1;
  }

  public int getNonDefaultModuleCount() {
    return myNonDefaultModuleStructures.size();
  }

  public List<QUnitModuleStructure> getNonDefaultModuleStructures() {
    return myNonDefaultModuleStructures;
  }

  public void addModuleStructure(@NotNull QUnitModuleStructure moduleStructure) {
    myNonDefaultModuleStructureByNameMap.put(moduleStructure.getName(), moduleStructure);
    myNonDefaultModuleStructures.add(moduleStructure);
  }

  @Nullable
  public AbstractQUnitModuleStructure getQUnitModuleByName(String qunitModuleName) {
    AbstractQUnitModuleStructure moduleStructure = myNonDefaultModuleStructureByNameMap.get(qunitModuleName);
    if (moduleStructure == null) {
      if (myDefaultModuleStructure.getName().equals(qunitModuleName)) {
        moduleStructure = myDefaultModuleStructure;
      }
    }
    return moduleStructure;
  }

  @NotNull
  public DefaultQUnitModuleStructure getDefaultModuleStructure() {
    return myDefaultModuleStructure;
  }

  public boolean hasQUnitSymbols() {
    return getDefaultModuleStructure().getTestCount() > 0 || getNonDefaultModuleCount() > 0;
  }

  @Nullable
  public QUnitModuleStructure findModuleStructureContainingOffset(int offset) {
    for (QUnitModuleStructure moduleStructure : myNonDefaultModuleStructures) {
      TextRange moduleTextRange = moduleStructure.getEnclosingCallExpression().getTextRange();
      if (JsPsiUtils.containsOffsetStrictly(moduleTextRange, offset)) {
        return moduleStructure;
      }
    }
    return null;
  }

  @Nullable
  public QUnitTestMethodStructure findTestMethodStructureContainingOffset(int offset) {
    QUnitTestMethodStructure testMethodStructure = myDefaultModuleStructure.findTestMethodStructureContainingOffset(
      offset);
    if (testMethodStructure != null) {
      return testMethodStructure;
    }
    for (QUnitModuleStructure moduleStructure : myNonDefaultModuleStructures) {
      testMethodStructure = moduleStructure.findTestMethodStructureContainingOffset(offset);
      if (testMethodStructure != null) {
        return testMethodStructure;
      }
    }
    return null;
  }

  @Override
  @Nullable
  public JstdRunElement findJstdRunElement(@NotNull TextRange textRange) {
    for (QUnitModuleStructure nonDefaultModuleStructure : myNonDefaultModuleStructures) {
      JstdRunElement jstdRunElement = nonDefaultModuleStructure.findJstdRunElement(textRange);
      if (jstdRunElement != null) {
        return jstdRunElement;
      }
    }
    return myDefaultModuleStructure.findJstdRunElement(textRange);
  }

  @Override
  public PsiElement findPsiElement(@NotNull String testCaseName, @Nullable String testMethodName) {
    AbstractQUnitModuleStructure qunitModuleStructure = getQUnitModuleByName(testCaseName);
    if (qunitModuleStructure != null) {
      if (testMethodName != null) {
        String name = removePrefix(testMethodName, "test ");
        QUnitTestMethodStructure test = qunitModuleStructure.getTestMethodStructureByName(name);
        if (test != null) {
          return test.getCallExpression();
        }
      } else {
        QUnitModuleStructure nonDefault = CastUtils.tryCast(qunitModuleStructure, QUnitModuleStructure.class);
        if (nonDefault != null) {
          return nonDefault.getEnclosingCallExpression();
        }
      }
    }
    return null;
  }

  @NotNull
  private static String removePrefix(@NotNull String s, @NotNull String prefix) {
    if (s.startsWith(prefix)) {
      return s.substring(prefix.length());
    }
    return s;
  }

}
