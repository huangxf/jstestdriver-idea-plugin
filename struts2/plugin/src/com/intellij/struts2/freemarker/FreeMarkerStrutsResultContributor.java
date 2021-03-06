/*
 * Copyright 2011 The authors
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.struts2.freemarker;

import com.intellij.freemarker.FreeMarkerApplicationComponent;
import com.intellij.freemarker.psi.files.FtlFileType;
import com.intellij.openapi.paths.PathReference;
import com.intellij.openapi.util.Comparing;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.struts2.dom.struts.impl.path.FileReferenceSetHelper;
import com.intellij.struts2.dom.struts.impl.path.StrutsResultContributor;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Provides path to {@code .ftl}-files.
 *
 * @author peter
 */
public class FreeMarkerStrutsResultContributor extends StrutsResultContributor {

  @NonNls
  public static final String FREEMARKER = "freemarker";

  @Override
  protected boolean matchesResultType(@NotNull @NonNls final String resultType) {
    return Comparing.equal(resultType, FREEMARKER);
  }

  public boolean createReferences(@NotNull final PsiElement psiElement,
                                  @NotNull final List<PsiReference> references,
                                  final boolean soft) {
    if (getNamespace(psiElement) == null) {
      return false;
    }

    final FileReferenceSet set = FileReferenceSetHelper.createRestrictedByFileType(psiElement, FtlFileType.INSTANCE);
    ContainerUtil.addAll(references, set.getAllReferences());
    return true;
  }

  public PathReference getPathReference(@NotNull final String path, @NotNull final PsiElement element) {
    return createDefaultPathReference(path, element, FreeMarkerApplicationComponent.FREEMARKER_ICON);
  }

}
