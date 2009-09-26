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

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Dynamically locates test cases to execute based on the context of the
 * provided TestFilter. The test suite provided by buildTestSuite() will execute
 * tests with the specified parallelism.
 *
 * @author rusty
 */
public class FireSuiteBuilder {

  private TestFilter filter;
  private int numParallelTests;

  /**
   * Test suites generated by this builder with execute two concurrent tests per
   * hardware thread.
   *
   * @param filter The definition of which tests to execute.
   */
  public FireSuiteBuilder(TestFilter filter) {
    this(filter, Runtime.getRuntime().availableProcessors() * 2);
  }

  /**
   * Test suites generated by this builder with execute with the specified
   * concurrency.
   *
   * @param filter The definition of which tests to execute.
   * @param numParallelTests The number of tests the resulting suite will excute
   *          concurrently.
   */
  public FireSuiteBuilder(TestFilter filter, int numParallelTests) {
    this.filter = filter;
    this.numParallelTests = numParallelTests;
  }

  /**
   * Builds the test suite defined by the provided TestFilter that will execute
   * tests with the specified concurrency.
   *
   * @return The test suite ready to be executed.
   */
  public Test buildTestSuite() {
    List<TestCase> testCases = new LinkedList<TestCase>();

    for (Class<?> testClass : findTestClasses()) {
      if (TestCase.class.isAssignableFrom(testClass)) {
        for (Method method : testClass.getDeclaredMethods()) {
          if (!filter.filterMethod(method)) {
            TestCase test;
            try {
              test = (TestCase) testClass.newInstance();
            } catch (InstantiationException e) {
              throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
              throw new RuntimeException(e);
            }
            test.setName(method.getName());
            testCases.add(test);
          }
        }
      }
    }

    TestSuite[] sequentialSuites = buildSequentialSuites(testCases);

    TestSuite activeSuite = new ActiveTestSuite();
    for (TestSuite sequentialSuite : sequentialSuites) {
      activeSuite.addTest(sequentialSuite);
    }

    return activeSuite;
  }

  private TestSuite[] buildSequentialSuites(List<TestCase> testCases) {
    TestSuite[] testSuites = new TestSuite[numParallelTests];
    for (int suiteIdx = 0; suiteIdx < testSuites.length; ++suiteIdx) {
      testSuites[suiteIdx] = new TestSuite();
    }
    for (int testIdx = 0; testIdx < testCases.size(); ++testIdx) {
      testSuites[testIdx % testSuites.length].addTest(testCases.get(testIdx));
    }
    return testSuites;
  }

  private List<Class<?>> findTestClasses() {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    String packageName = filter.getPackageRoot();
    URL url = classLoader.getResource(packageName.replaceAll("\\.", "/"));

    List<File> classFiles = findClassFiles(new File(url.getFile()));
    List<Class<?>> testClasses = new LinkedList<Class<?>>();
    for (File classFile : classFiles) {
      String className = classFile.getPath().replaceAll("/", ".");
      className = className.substring(0, className.lastIndexOf(".class"));
      className = className.substring(className.indexOf(packageName));

      Class<?> testClass;
      try {
        testClass = Class.forName(className);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        continue;
      }

      if (TestCase.class.isAssignableFrom(testClass)
          && !filter.filterClass(testClass)) {
        testClasses.add(testClass);
      }
    }
    return testClasses;
  }

  private List<File> findClassFiles(File directory) {
    File[] startingFiles = directory.listFiles();
    List<File> resultFiles = new LinkedList<File>();
    for (File file : startingFiles) {
      if (file.isDirectory()) {
        resultFiles.addAll(findClassFiles(file));
      } else if (file.getName().endsWith(".class")) {
        resultFiles.add(file);
      }
    }
    return resultFiles;
  }
}