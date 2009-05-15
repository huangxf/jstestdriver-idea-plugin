/*
 * Copyright (c) 2007-2009, Osmorc Development Team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright notice, this list
 *       of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this
 *       list of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *     * Neither the name of 'Osmorc Development Team' nor the names of its contributors may be
 *       used to endorse or promote products derived from this software without specific
 *       prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.osmorc.settings;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.osmorc.frameworkintegration.FrameworkInstanceDefinition;

import java.util.List;


/**
 * @author Robert F. Beeger (robert@beeger.net)
 */
public class ApplicationSettingsTest
{
  @Before
  public void setUp()
  {
    _testObject = new ApplicationSettings();
    _frameworkInstanceDefinition = new FrameworkInstanceDefinition();
    _frameworkInstanceDefinition.setName("T");
    _frameworkInstanceDefinition.setFrameworkIntegratorName("i");
    _frameworkInstanceDefinition.setBaseFolder("b");
    _testObject.addFrameworkInstanceDefinition(_frameworkInstanceDefinition);
  }

  @Test
  public void testCreateCopy()
  {
    ApplicationSettings copy = _testObject.createCopy();

    assertNotSame(copy, _testObject);
    assertNotSame(copy.getFrameworkInstanceDefinitions(), _testObject.getFrameworkInstanceDefinitions());

    List<FrameworkInstanceDefinition> orgDefinitions = _testObject.getFrameworkInstanceDefinitions();
    List<FrameworkInstanceDefinition> copiedDefinitions = copy.getFrameworkInstanceDefinitions();

    assertThat(orgDefinitions.size(), equalTo(1));
    assertThat(copiedDefinitions.size(), equalTo(1));
    assertNotSame(orgDefinitions.get(0), copiedDefinitions.get(0));
    assertEquals(orgDefinitions.get(0), copiedDefinitions.get(0));
  }
  
  @Test
  public void testGetFrameworkInstance()
  {
    FrameworkInstanceDefinition frameworkInstance =
        _testObject.getFrameworkInstance(_frameworkInstanceDefinition.getName());
    assertThat(frameworkInstance, sameInstance(_frameworkInstanceDefinition));
  }

  private ApplicationSettings _testObject;
  private FrameworkInstanceDefinition _frameworkInstanceDefinition;
}