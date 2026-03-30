# Testing Setup And Runbook (App Unit Tests)

Date: 2026-02-24
Project: `C:\d\androidstudio\AstrosageKundliLiveStreamingNew`

## Goal
- Enable running tests on demand.
- Avoid APK/AAB build testing as a goal.
- Avoid using library test suites.
- Add one working sample test and verify execution.

## Changes Made

1. Removed Java 17 Gradle JVM blocker:
- File: `gradle.properties`
- Change:
  - Before:
    - `org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=4096m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8`
  - After:
    - `org.gradle.jvmargs=-Xmx4096m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8`

2. Added app unit test dependency:
- File: `app/build.gradle`
- Added:
  - `testImplementation 'junit:junit:4.13.2'`

3. Added sample unit test class:
- File: `app/src/test/java/com/ojassoft/astrosage/utils/ValidatorTest.java`
- Contains 2 tests:
  - `validVisa_passesValidation`
  - `invalidVisa_failsValidation`

## Commands Run

1. Full app unit test task:
- Command:
  - `gradlew.bat :app:testDebugUnitTest --console=plain`
- Result:
  - `BUILD SUCCESSFUL`

2. Run only one test class:
- Command:
  - `gradlew.bat :app:testDebugUnitTest --tests com.ojassoft.astrosage.utils.ValidatorTest --console=plain`
- Result:
  - `BUILD SUCCESSFUL`

3. Run only one test method:
- Command:
  - `gradlew.bat :app:testDebugUnitTest --tests com.ojassoft.astrosage.utils.ValidatorTest.validVisa_passesValidation --console=plain`
- Result:
  - `BUILD SUCCESSFUL`

## Test Result Artifacts

- HTML report:
  - `app/build/reports/tests/testDebugUnitTest/index.html`
- XML result:
  - `app/build/test-results/testDebugUnitTest/TEST-com.ojassoft.astrosage.utils.ValidatorTest.xml`
- Verified XML summary:
  - `tests="2"`
  - `failures="0"`
  - `errors="0"`
  - `skipped="0"`

## Important Notes

1. Scope:
- No APK/AAB build tests were created.
- No library test suites were enabled or run.

2. Gradle behavior:
- `:app:testDebugUnitTest` may still compile dependent modules as part of task graph.
- This is normal for Android Gradle dependency resolution.
- It does not mean library test suites are being executed.

3. Windows `cmd` filter usage:
- `--tests` filter worked reliably when passed without extra quoted escaping in this environment.
- Working examples:
  - `--tests com.ojassoft.astrosage.utils.ValidatorTest`
  - `--tests com.ojassoft.astrosage.utils.ValidatorTest.validVisa_passesValidation`

## Current Baseline

- App unit test infrastructure is now runnable.
- Sample test execution is verified end-to-end.
- You can now add more tests under:
  - `app/src/test/java`

## Android Studio Setup Notes (For New Developers)

### 1) Project window dropdown: `Project / Packages / Project Files / Tests`

- This dropdown only changes how files are shown in the left Project panel.
- `Tests` view filters navigation to test sources (`src/test`, `src/androidTest`).
- It does **not** run tests and does not change build behavior.

### 2) Which run profile to create: `JUnit` or `Android Instrumented Tests`

- Choose **`JUnit`** for local unit tests in:
  - `app/src/test/java`
- Choose **`Android Instrumented Tests`** only for:
  - `app/src/androidTest/java`
  - (requires device/emulator)

For current setup and sample test, use **JUnit**.

### 3) How to create a working JUnit run configuration

1. Open `Run/Debug Configurations`.
2. Click `+` and choose `JUnit`.
3. Set:
   - `Name`: `ValidatorTest (unit)` (or any name)
   - `Build and run` module: `app`
   - `Test kind`: `Class`
   - `Class`: `com.ojassoft.astrosage.utils.ValidatorTest`
   - `Working directory`: `$PROJECT_DIR$` (or default once module is set)
4. Click `Apply` and `Run`.

If module dropdown shows `module not specified`, do:
1. `File > Sync Project with Gradle Files`
2. Ensure Gradle JVM is JDK 17
3. Reopen the configuration dialog

### 4) If test file shows red imports (`Cannot resolve symbol Test/Assert`)

This is usually IDE sync/index state (not Gradle config), because CLI tests already pass.

Fix steps:
1. `File > Sync Project with Gradle Files`
2. Check `Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JVM` = JDK 17
3. Ensure Gradle offline mode is OFF
4. If still red: `File > Invalidate Caches / Restart`
5. Verify dependency exists in `app/build.gradle`:
   - `testImplementation 'junit:junit:4.13.2'`

### 5) Reliable fallback run config (Gradle type)

If JUnit run profile in IDE is unstable, create **Gradle** run config:
- Task:
  - `:app:testDebugUnitTest --tests com.ojassoft.astrosage.utils.ValidatorTest`

This exactly matches the verified CLI flow.

## Testing Approach Guidance (App Team Decision)

1. Scope for now:
- Focus on app unit tests only.
- Do not create APK/AAB test flows.
- Do not rely on library module test suites.

2. For Android-dependent code:
- Use **JUnit + Mockito** for pure business logic and collaborators.
- Use **Robolectric** for classes needing Android APIs (`Context`, resources, etc.) but still fast local execution.
- Use **Instrumented tests** only for true device/emulator integration behavior.

## Real Business Test Added (Phase-1)

### Business rule selected

- Domain: AI Pass subscriptions and coupon mapping.
- Source logic: `CUtils.getAIPassCouponByPlanServiceId(String planServiceId)`.
- Why this is business-critical:
  - Wrong mapping can apply incorrect coupons for paid plans.
  - Impacts pricing, offers, and conversion flow for AI Pass plans.

### Test file

- `app/src/test/java/com/ojassoft/astrosage/varta/utils/CUtilsBusinessRulesTest.java`

### Cases covered

1. Correct mapping for known plan service IDs:
- `PLAN_SERVICE_ID_259` -> `AIPASS1`
- `PLAN_SERVICE_ID_262` -> `AIPASS2`
- `PLAN_SERVICE_ID_260` -> `AIPASS3`

2. Safety behavior for unsupported input:
- Unknown plan ID returns empty string.
- Null plan ID returns empty string.

### Run command used

- `gradlew.bat :app:testDebugUnitTest --tests com.ojassoft.astrosage.varta.utils.CUtilsBusinessRulesTest --console=plain`

### Verified result

- XML report:
  - `app/build/test-results/testDebugUnitTest/TEST-com.ojassoft.astrosage.varta.utils.CUtilsBusinessRulesTest.xml`
- Summary:
  - `tests="2"`
  - `failures="0"`
  - `errors="0"`
  - `skipped="0"`

## Test Suite Added

### Purpose

- Run a fixed, curated set of important tests together.
- Avoid package-wide auto inclusion when team wants explicit control.

### Suite file

- `app/src/test/java/com/ojassoft/astrosage/SmokeTestSuite.java`

### Included test classes

1. `com.ojassoft.astrosage.utils.ValidatorTest`
2. `com.ojassoft.astrosage.varta.utils.CUtilsBusinessRulesTest`
3. `com.ojassoft.astrosage.varta.utils.UPIResponseParserTest`

### Gradle command

- `gradlew.bat :app:testDebugUnitTest --tests com.ojassoft.astrosage.SmokeTestSuite --console=plain`

### Verified result

- Build result:
  - `BUILD SUCCESSFUL`

### Android Studio run profile for suite

1. `Run > Edit Configurations`
2. `+` -> `JUnit`
3. Set:
   - `Name`: `Smoke Suite`
   - `Module`: `app`
   - `Test kind`: `Class`
   - `Class`: `com.ojassoft.astrosage.SmokeTestSuite`
4. `Apply` -> `Run`

## Robolectric Test Added (Android Framework Class Case)

### Why this test exists

- Requirement: test code that uses Android framework classes without emulator/device.
- Solution used: **Robolectric** local unit test under `src/test/java`.

### Build setup used

- File: `app/build.gradle`
- Enabled Android resources for local unit tests:
  - `testOptions { unitTests { includeAndroidResources = true } }`
- Added dependencies:
  - `testImplementation 'junit:junit:4.13.2'`
  - `testImplementation 'org.robolectric:robolectric:4.13'`

### Test file

- `app/src/test/java/com/ojassoft/astrosage/varta/utils/UPIResponseParserTest.java`

### Business behavior covered

1. Valid UPI response parses expected fields.
2. Parsing behavior for pre-Nougat branch.
3. Null `Intent` returns failure status.

### Important Robolectric stability note

- Project app class initializes Firebase and can fail in Robolectric.
- In this test we override app class in `@Config`:
  - `application = android.app.Application.class`
- This avoids Firebase startup side effects during local unit tests.

### Verified run commands

1. Single class:
   - `gradlew.bat :app:testDebugUnitTest --tests com.ojassoft.astrosage.varta.utils.UPIResponseParserTest --console=plain`
2. Through suite:
   - `gradlew.bat :app:testDebugUnitTest --tests com.ojassoft.astrosage.SmokeTestSuite --console=plain`

Both were verified with `BUILD SUCCESSFUL`.

## Android Studio Runner Guidance (Important)

### Symptom seen in IDE

- Running suite from a misconfigured JUnit run configuration produced:
  - `java.lang.NoSuchMethodError: org.junit.runners.model.FrameworkMethod.getDeclaringClass()`

### Root cause

- IDE JUnit runner used incompatible classpath because configuration had:
  - `module not specified`
  - `-cp <no module>`

### Recommended fix

1. Use **Gradle** run configuration (preferred for Robolectric consistency):
   - `Run > Edit Configurations > + > Gradle`
   - Task: `:app:testDebugUnitTest`
   - Arguments: `--tests com.ojassoft.astrosage.SmokeTestSuite`
2. Or if using JUnit config, set module to `app` (never `<no module>`).
3. In Android Studio settings:
   - `Settings > Build, Execution, Deployment > Build Tools > Gradle > Run tests using: Gradle`

This keeps IDE behavior aligned with the CLI commands that already pass.

## Crash Fix Note: `ActAppModule.onActivityResult` NullPointerException

Date: 2026-03-19
Project: `D:\OJAS PROJECT\AkApril25`

### Reported crash

- Exception:
  - `java.lang.RuntimeException: Failure delivering result ... to activity com.ojassoft.astrosage.ui.act.ActAppModule`
- Root cause:
  - `java.lang.NullPointerException: Attempt to invoke virtual method 'boolean java.lang.String.equals(java.lang.Object)' on a null object reference`
- Crash site:
  - `com.ojassoft.astrosage.ui.act.ActAppModule.onActivityResult`

### Why the crash happened

- The result flow returned to `ActAppModule` with request code `2001`.
- The routing branch for `"openProfileForChat"` or `"openKundliList"` was entered.
- In that branch, code assumed `ChatUtils.getInstance(this).consultationType` was always non-null.
- For some flows, especially when `AstrosageKundliApplication.selectedAstrologerDetailBean` is null and the result only carries fallback extras, `consultationType` can be null.
- The old code then executed:
  - `ChatUtils.getInstance(this).consultationType.equals(TYPE_AI_CHAT_RANDOM)`
- That produced the NPE.

### Important codebase behavior preserved

- This codebase uses presence of result keys like `"openProfileForChat"` and `"openKundliList"` as route markers.
- The fix does not change that contract to boolean-only routing, because child activities may return these keys with `false` values while still expecting parent-side routing behavior.

### Changes made

Files updated:

- `app/src/main/java/com/ojassoft/astrosage/ui/act/ActAppModule.java`
- `app/src/main/java/com/ojassoft/astrosage/varta/ui/activity/DashBoardActivity.java`

Implementation details:

1. Extracted safer fallback values from the result `Intent`:
   - `fromWhere`
   - `urlText`
   - `consultationType`

2. Added consultation type resolution order:
   - first use `data.getStringExtra("consultationType")`
   - if empty and `urlText` is `TYPE_AI_CHAT_RANDOM`, use `urlText`
   - if still empty and `fromWhere` is `TYPE_AI_CHAT_RANDOM`, use `fromWhere`
   - if still empty, fall back to `ChatUtils.getInstance(this).consultationType`

3. Replaced null-unsafe comparisons:
   - before:
     - `consultationType.equals(TYPE_AI_CHAT_RANDOM)`
   - after:
     - `TextUtils.equals(consultationType, TYPE_AI_CHAT_RANDOM)`

4. Reused the resolved `consultationType` when reopening:
   - `openSavedKundliList(...)`
   - `openProfileForChat(...)`

5. Applied the same protection in `DashBoardActivity` because it had the same crash pattern in its corresponding result handling path.

### Why `DashBoardActivity` was updated too

- `DashBoardActivity` contained equivalent result-routing logic.
- Without mirroring the fix there, the same NPE could appear from another entry point even if `ActAppModule` was fixed.

### Quick verification checklist

1. Trigger the flow that returns from saved kundli/profile chat into `ActAppModule`.
2. Verify app does not crash when:
   - `selectedAstrologerDetailBean == null`
   - `consultationType` is missing from result extras
3. Verify parent activity still reopens the correct screen for:
   - AI random chat
   - normal profile-for-chat flow
   - saved kundli selection flow
4. Repeat the same navigation from the dashboard entry path.

### Build verification note

- Command attempted:
  - `.\gradlew.bat :app:compileDebugKotlin --offline`
- Result:
  - compile verification was blocked by a local Kotlin incremental cache / daemon issue
  - no source syntax failure from the patched logic was reported before that environment-level failure
