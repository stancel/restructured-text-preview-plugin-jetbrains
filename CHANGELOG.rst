.. _rst-preview-changelog:

######################################################################
Changelog
######################################################################

All notable changes to the ReStructuredText Preview Plugin will be documented
in this file.

The format is based on `Keep a Changelog <https://keepachangelog.com/>`_,
and this project adheres to `Semantic Versioning <https://semver.org/>`_.

.. contents:: Versions
   :local:
   :depth: 1

.. _changelog-unreleased:

**********************************************************************
[Unreleased]
**********************************************************************

.. _changelog-unreleased-planned:

Planned
======================================================================

- Support for Sphinx-specific directives
- Synchronized scrolling between editor and preview

.. _changelog-1.1.0:

**********************************************************************
[1.1.0] - 2026-05-24
**********************************************************************

.. _changelog-1.1.0-added:

Added
======================================================================

- **Print preview** feature for rendered RST output (JCEF uses native
  Chromium print dialog, Swing uses Java print API)
- **Admonition styling** for RST directives (``note``, ``warning``,
  ``tip``, ``important``, ``caution``, ``danger``, ``error``, ``hint``,
  ``attention``) with themed colors for both light and dark modes
- **Print CSS** with optimized layout for paper output (white background,
  black text, no max-width constraint, code block wrapping)
- Print action available in the editor right-click context menu

.. _changelog-1.1.0-changed:

Changed
======================================================================

- Extended IDE compatibility from 2025.3 to **2026.1** (build 261.*)
- Updated Gradle wrapper from 8.13 to **9.5.1**
- Updated IntelliJ Platform Gradle Plugin from 2.10.5 to **2.16.0**
- Added plugin verification against IntelliJ IDEA 2026.1 (unified
  distribution)
- Increased preview debounce from 50ms to **300ms** to reduce unnecessary
  rst2html process spawns during fast typing

.. _changelog-1.1.0-fixed:

Fixed
======================================================================

- Added content hash check to skip redundant rst2html calls when
  document text has not changed (cursor movement, selection changes)

.. _changelog-1.0.3:

**********************************************************************
[1.0.3] - 2026-01-19
**********************************************************************

.. _changelog-1.0.3-fixed:

Fixed
======================================================================

- Replaced deprecated ``TextFieldWithBrowseButton.addBrowseFolderListener()`` API
- Replaced deprecated ``FileChooserDescriptorFactory.createSingleFileDescriptor()`` API
- Replaced deprecated ``UIUtil.isUnderDarcula()`` with ``JBColor.isBright()``
- Replaced deprecated ``URL(String)`` constructor with ``URI`` API
- Replaced deprecated ``Disposer.isDisposed()`` with manual disposal tracking
- Fixed non-extendable API violation (removed ``CheckedDisposable`` implementation)

.. _changelog-1.0.3-changed:

Changed
======================================================================

- Updated development workflow in CLAUDE.md to require ``verifyPlugin`` before commits
- Added ``@SuppressWarnings("UnstableApiUsage")`` for intentional experimental API usage
- Updated Gradle wrapper from 8.5 to 8.13
- Updated IntelliJ Platform Gradle Plugin from 2.2.1 to 2.10.5

.. _changelog-1.0.2:

**********************************************************************
[1.0.2] - 2026-01-19
**********************************************************************

.. _changelog-1.0.2-added:

Added
======================================================================

- **Plugin icon** for JetBrains Marketplace and IDE plugin list
- Light and dark theme icon variants (``pluginIcon.svg``, ``pluginIcon_dark.svg``)

.. _changelog-1.0.1:

**********************************************************************
[1.0.1] - 2026-01-19
**********************************************************************

.. _changelog-1.0.1-added:

Added
======================================================================

- **Automated JetBrains Marketplace publishing** via GitHub Actions
- **Plugin verification** configuration in build system

.. _changelog-1.0.1-changed:

Changed
======================================================================

- Updated ``build.gradle.kts`` with ``pluginVerification.ides`` configuration
- Enabled ``publishPlugin`` step in ``release.yml`` workflow

.. _changelog-1.0.0:

**********************************************************************
[1.0.0] - 2025-01-19
**********************************************************************

Initial release of the ReStructuredText Preview Plugin.

.. _changelog-1.0.0-added:

Added
======================================================================

- **Split-pane editor** with RST source and HTML preview
- **Live preview updates** on document changes
- **JCEF-based rendering** using embedded Chromium browser
- **Swing fallback renderer** for platforms without JCEF support
- **Automatic rst2html detection** from:

  - PATH environment variable
  - pyenv shims (``~/.pyenv/shims/rst2html``)
  - pyenv versions (``~/.pyenv/versions/*/bin/rst2html``)
  - Local bin (``~/.local/bin/rst2html``)
  - System locations (``/usr/bin/rst2html``, ``/usr/local/bin/rst2html``)

- **Settings page** at Languages & Frameworks → ReStructuredText Preview:

  - Configurable ``rst2html`` executable path
  - Choice between JCEF and Swing renderers

- **Theme support** - automatic dark/light CSS switching
- **Built-in CSS** for clean document rendering
- **External link handling** - opens links in system browser
- **Internal anchor navigation** - scroll to anchors within preview
- **Image path resolution** - converts relative paths to absolute

.. _changelog-1.0.0-technical:

Technical Details
----------------------------------------------------------------------

- Plugin ID: ``com.github.stancel.rst-preview-standalone``
- Depends on: ``org.jetbrains.plugins.rest`` (ReStructuredText language support)
- IDE Build Range: 243 - 253.*
- Java Version: 21

.. _changelog-1.0.0-files:

Files in Release
----------------------------------------------------------------------

::

    src/main/java/com/github/stancel/rstpreview/editor/
    ├── RstPreviewEditorProvider.java   # Creates split editor
    ├── RstPreviewFileEditor.java       # Preview pane controller
    ├── RstPreviewPanel.java            # Panel interface
    ├── RstJcefPreviewPanel.java        # JCEF renderer
    ├── RstSwingPreviewPanel.java       # Swing fallback
    ├── RstPreviewProvider.java         # RST→HTML conversion
    ├── RstPreviewSettings.java         # Settings persistence
    └── RstPreviewConfigurable.java     # Settings UI

    src/main/resources/META-INF/
    └── plugin.xml                      # Plugin configuration
