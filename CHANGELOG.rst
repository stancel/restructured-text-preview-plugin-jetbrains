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
