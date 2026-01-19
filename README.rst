.. _rst-preview-plugin:

######################################################################
ReStructuredText Preview Plugin for JetBrains IDEs
######################################################################

.. contents:: Table of Contents
   :local:
   :depth: 2

.. _rst-preview-overview:

**********************************************************************
Overview
**********************************************************************

A standalone plugin that adds **live HTML preview** for ReStructuredText
(``.rst``) files in JetBrains IDEs (PHPStorm, IntelliJ IDEA, PyCharm, WebStorm,
etc.) **without requiring the Python plugin**.

.. _rst-preview-problem:

The Problem
======================================================================

JetBrains' built-in ReStructuredText plugin requires the Python plugin to
render previews. This is problematic for:

- PHPStorm users (no Python plugin available)
- Users who don't want/need the full Python plugin
- Users whose IDE doesn't bundle the RST preview functionality

.. _rst-preview-solution:

The Solution
======================================================================

This plugin calls your system's ``rst2html`` command (from docutils) directly,
bypassing the need for the Python plugin entirely. The preview renders inside
the IDE using JCEF (embedded Chromium), providing a native split-pane editing
experience.

.. _rst-preview-features:

**********************************************************************
Features
**********************************************************************

- **Live Preview**: See rendered HTML as you type
- **Split-Pane Editor**: Source on left, preview on right
- **JCEF Rendering**: High-quality HTML5/CSS rendering using embedded Chromium
- **Theme Support**: Automatic dark/light theme switching
- **Configurable**: Custom ``rst2html`` path in settings
- **Auto-Detection**: Finds ``rst2html`` from pyenv, PATH, common locations
- **Fallback Renderer**: Swing-based rendering if JCEF unavailable

.. _rst-preview-requirements:

**********************************************************************
Requirements
**********************************************************************

.. _rst-preview-req-ide:

IDE Requirements
======================================================================

- Any JetBrains IDE (2024.3+)
- Build range: 243 - 253.*

.. _rst-preview-req-system:

System Requirements
======================================================================

- **docutils** installed with ``rst2html`` command available

Install docutils:

.. code-block:: bash

   pip install docutils

If using pyenv:

.. code-block:: bash

   # Ensure your Python version has docutils
   pyenv global 3.13.0  # or your version
   pip install docutils

.. _rst-preview-installation:

**********************************************************************
Installation
**********************************************************************

.. _rst-preview-install-disk:

From Disk (Manual)
======================================================================

1. Download the latest release ZIP from GitHub Releases
2. Open your JetBrains IDE
3. Go to **Settings** → **Plugins** → **⚙️** → **Install Plugin from Disk...**
4. Select the downloaded ``.zip`` file
5. Restart the IDE

.. _rst-preview-install-marketplace:

From JetBrains Marketplace
======================================================================

1. Open your JetBrains IDE
2. Go to **Settings** → **Plugins** → **Marketplace**
3. Search for "ReStructuredText Preview"
4. Click **Install**
5. Restart the IDE

.. _rst-preview-usage:

**********************************************************************
Usage
**********************************************************************

.. _rst-preview-usage-basic:

Basic Usage
======================================================================

Simply open any ``.rst`` file. The editor will display:

- **Left pane**: RST source code (editable)
- **Right pane**: Live HTML preview

The preview updates automatically as you type.

.. _rst-preview-usage-controls:

Editor Controls
======================================================================

Use the toolbar buttons to:

- Toggle between editor-only, preview-only, or split view
- Refresh the preview manually

.. _rst-preview-configuration:

**********************************************************************
Configuration
**********************************************************************

Access settings at: **Settings** → **Languages & Frameworks** →
**ReStructuredText Preview**

.. _rst-preview-config-path:

rst2html Path
======================================================================

The plugin auto-detects ``rst2html`` from common locations:

1. ``PATH`` environment variable
2. ``~/.pyenv/shims/rst2html``
3. ``~/.pyenv/versions/*/bin/rst2html``
4. ``~/.local/bin/rst2html``
5. ``/usr/bin/rst2html``
6. ``/usr/local/bin/rst2html``

To override, enter the full path to ``rst2html`` in settings.

.. _rst-preview-config-renderer:

Preview Panel Type
======================================================================

- **JCEF (recommended)**: Full HTML5/CSS support via embedded Chromium
- **Swing**: Basic HTML rendering, fallback for unsupported platforms

.. _rst-preview-building:

**********************************************************************
Building from Source
**********************************************************************

.. _rst-preview-build-prereq:

Prerequisites
======================================================================

- JDK 21 or higher
- Gradle (wrapper included)

.. _rst-preview-build-commands:

Build Commands
======================================================================

.. code-block:: bash

   # Build the plugin
   ./gradlew build

   # Create distributable ZIP
   ./gradlew buildPlugin
   # Output: build/distributions/rst-preview-plugin-X.X.X.zip

   # Run IDE with plugin for testing
   ./gradlew runIde

   # Clean build artifacts
   ./gradlew clean

.. _rst-preview-contributing:

**********************************************************************
Contributing
**********************************************************************

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run ``./gradlew build`` to verify
5. Submit a pull request

.. _rst-preview-license:

**********************************************************************
License
**********************************************************************

This project is licensed under the **Apache License 2.0**.

See the ``LICENSE`` file for details.

.. _rst-preview-acknowledgments:

**********************************************************************
Acknowledgments
**********************************************************************

- JetBrains for the IntelliJ Platform SDK
- The docutils project for ``rst2html``
- Inspired by the original JetBrains ReStructuredText plugin

.. _rst-preview-links:

**********************************************************************
Links
**********************************************************************

- `GitHub Repository <https://github.com/stancel/rst-preview-jetbrains>`_
- `JetBrains Marketplace <https://plugins.jetbrains.com/>`_
- `docutils Documentation <https://docutils.sourceforge.io/>`_
- `Issue Tracker <https://github.com/stancel/rst-preview-jetbrains/issues>`_
