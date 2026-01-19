.. _publishing-guide:

######################################################################
Publishing to JetBrains Marketplace
######################################################################

This guide explains how to publish the ReStructuredText Preview Plugin to the
JetBrains Marketplace for public distribution.

.. contents:: Table of Contents
   :local:
   :depth: 2

.. _publishing-overview:

**********************************************************************
Overview
**********************************************************************

The JetBrains Marketplace is the official distribution channel for IntelliJ
platform plugins. Publishing there allows users to install the plugin directly
from within their IDE.

**Marketplace URL**: https://plugins.jetbrains.com/

.. _publishing-prerequisites:

**********************************************************************
Prerequisites
**********************************************************************

.. _publishing-prereq-account:

JetBrains Account
======================================================================

1. Create a JetBrains Account at https://account.jetbrains.com/
2. This account will be your plugin vendor identity

.. _publishing-prereq-token:

Marketplace Token
======================================================================

1. Go to https://plugins.jetbrains.com/author/me/tokens
2. Click **Generate Token**
3. Give it a descriptive name (e.g., "GitHub Actions")
4. Copy the token (you won't see it again!)
5. Store securely - this is used for automated publishing

.. _publishing-prereq-plugin:

Plugin Requirements
======================================================================

Before publishing, ensure:

- [ ] Plugin builds successfully: ``./gradlew build``
- [ ] Plugin verification passes: ``./gradlew verifyPlugin``
- [ ] ``plugin.xml`` has valid:

  - Unique plugin ID
  - Descriptive name
  - Version number
  - Description
  - Vendor information
  - Compatibility range (sinceBuild/untilBuild)

- [ ] LICENSE file present
- [ ] README documentation complete

.. _publishing-manual:

**********************************************************************
Manual Publishing
**********************************************************************

.. _publishing-manual-build:

Build the Plugin
======================================================================

.. code-block:: bash

   # Clean previous builds
   ./gradlew clean

   # Build the distributable ZIP
   ./gradlew buildPlugin

   # Verify the plugin
   ./gradlew verifyPlugin

The plugin ZIP will be at: ``build/distributions/rst-preview-plugin-X.X.X.zip``

.. _publishing-manual-upload:

Upload to Marketplace
======================================================================

1. Go to https://plugins.jetbrains.com/
2. Sign in with your JetBrains account
3. Click **Upload plugin** (or go to your profile → Add Plugin)
4. Select the ``.zip`` file from ``build/distributions/``
5. Fill in any additional details
6. Submit for review

.. _publishing-manual-review:

Review Process
======================================================================

- JetBrains reviews new plugins for security and quality
- First review typically takes 1-3 business days
- Updates to existing plugins are usually faster
- You'll receive email notification of approval or issues

.. _publishing-automated:

**********************************************************************
Automated Publishing (GitHub Actions)
**********************************************************************

.. _publishing-automated-setup:

Setup
======================================================================

1. **Add the publish token as a secret:**

   - Go to your GitHub repository
   - Navigate to **Settings** → **Secrets and variables** → **Actions**
   - Click **New repository secret**
   - Name: ``PUBLISH_TOKEN``
   - Value: Your JetBrains Marketplace token

2. **Update build.gradle.kts:**

   Add the publishing configuration:

   .. code-block:: kotlin

      intellijPlatform {
          publishing {
              token = providers.environmentVariable("PUBLISH_TOKEN")
          }
      }

3. **Enable in workflow:**

   Edit ``.github/workflows/release.yml`` and uncomment the publish step:

   .. code-block:: yaml

      - name: Publish to JetBrains Marketplace
        run: ./gradlew publishPlugin
        env:
          PUBLISH_TOKEN: ${{ secrets.PUBLISH_TOKEN }}

.. _publishing-automated-release:

Creating a Release
======================================================================

To trigger an automated release:

.. code-block:: bash

   # Update version in build.gradle.kts
   # Update CHANGELOG.rst

   # Commit changes
   git add .
   git commit -m "chore: Prepare release v1.0.1"

   # Create and push tag
   git tag v1.0.1
   git push origin main --tags

This will:

1. Trigger the release workflow
2. Build the plugin
3. Create a GitHub Release with the ZIP
4. Publish to JetBrains Marketplace (if enabled)

.. _publishing-version:

**********************************************************************
Versioning
**********************************************************************

.. _publishing-version-scheme:

Version Scheme
======================================================================

Use semantic versioning: ``MAJOR.MINOR.PATCH``

- **MAJOR**: Breaking changes or major new features
- **MINOR**: New features, backward compatible
- **PATCH**: Bug fixes, backward compatible

.. _publishing-version-update:

Updating Version
======================================================================

1. Update ``build.gradle.kts``:

   .. code-block:: kotlin

      version = "1.0.1"

2. Update ``CHANGELOG.rst`` with new version section

3. Commit with message like: ``chore: Bump version to 1.0.1``

.. _publishing-compatibility:

**********************************************************************
IDE Compatibility
**********************************************************************

.. _publishing-compatibility-range:

Build Range
======================================================================

In ``build.gradle.kts``:

.. code-block:: kotlin

   intellijPlatform {
       pluginConfiguration {
           ideaVersion {
               sinceBuild = "243"        // Minimum IDE build
               untilBuild = "253.*"      // Maximum IDE build
           }
       }
   }

.. _publishing-compatibility-matrix:

IDE Version Matrix
======================================================================

+------------------+---------------+
| IDE Version      | Build Number  |
+==================+===============+
| 2024.3           | 243.*         |
+------------------+---------------+
| 2025.1           | 251.*         |
+------------------+---------------+
| 2025.2           | 252.*         |
+------------------+---------------+
| 2025.3           | 253.*         |
+------------------+---------------+

Update ``untilBuild`` when new IDE versions are released and tested.

.. _publishing-troubleshooting:

**********************************************************************
Troubleshooting
**********************************************************************

.. _publishing-trouble-verification:

Verification Failures
======================================================================

If ``./gradlew verifyPlugin`` fails:

- Check plugin.xml syntax
- Ensure all required fields are present
- Verify sinceBuild/untilBuild range
- Check for deprecated API usage

.. _publishing-trouble-publish:

Publishing Failures
======================================================================

Common issues:

- **Invalid token**: Regenerate and update secret
- **Version exists**: Increment version number
- **Compatibility issues**: Check sinceBuild/untilBuild

.. _publishing-trouble-review:

Review Rejection
======================================================================

Common reasons:

- Security issues in code
- Missing privacy policy (if collecting data)
- Incomplete plugin description
- Plugin doesn't work as described

Fix the issues and resubmit.

.. _publishing-resources:

**********************************************************************
Resources
**********************************************************************

- `IntelliJ Platform Plugin SDK <https://plugins.jetbrains.com/docs/intellij/>`_
- `Marketplace Publishing Guide <https://plugins.jetbrains.com/docs/marketplace/>`_
- `Plugin Signing <https://plugins.jetbrains.com/docs/intellij/plugin-signing.html>`_
- `Gradle IntelliJ Plugin <https://github.com/JetBrains/intellij-platform-gradle-plugin>`_
