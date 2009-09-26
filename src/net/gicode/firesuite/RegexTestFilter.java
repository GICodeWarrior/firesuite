/**
 * Copyright 2009 Rusty Burchfield
 *
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
 *
 * This file is part of Firesuite.
 */
package net.gicode.firesuite;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * Defines a filter to include only the matched by the provided regex for class
 * and (optionally) method.
 *
 * @author rusty
 */
public class RegexTestFilter extends DefaultTestFilter {

  private Pattern classPattern;
  private Pattern methodPattern;

  /**
   * @param packageRoot The package name to search under.
   * @param regex A regex that matches the test classes to execute.
   */
  public RegexTestFilter(String packageRoot, String class_regex) {
    this(packageRoot, class_regex, null);
  }
  
  public RegexTestFilter(String packageRoot, String classRegex, String methodRegex) {
    super(packageRoot);
    classPattern = Pattern.compile(classRegex);
    methodPattern = (methodRegex != null) ?
        Pattern.compile(methodRegex) : Pattern.compile("");
  }

  @Override
  public boolean filterClass(Class<?> testClass) {
    return !classPattern.matcher(testClass.getCanonicalName()).find();
  }

  @Override
  public boolean filterMethod(Method testMethod) {
    if (!methodPattern.matcher(testMethod.getName()).find()){
      return true;
    }
    return super.filterMethod(testMethod);
  }

}
