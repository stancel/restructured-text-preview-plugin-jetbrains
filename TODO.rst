.. _rst-preview-todo:

######################################################################
TODO - Planned Improvements
######################################################################

This document tracks planned features, improvements, and known issues for the
ReStructuredText Preview Plugin.

.. contents:: Categories
   :local:
   :depth: 2

.. _todo-high-priority:

**********************************************************************
High Priority
**********************************************************************

No high priority items at this time. See Medium Priority for planned features.

.. _todo-medium-priority:

**********************************************************************
Medium Priority
**********************************************************************

.. _todo-sphinx-support:

Sphinx Directive Support
======================================================================

- [ ] Handle ``.. code-block::`` with syntax highlighting
- [ ] Support ``.. note::``, ``.. warning::``, ``.. tip::`` admonitions
- [ ] Process ``.. toctree::`` directives
- [ ] Handle ``:ref:`` cross-references
- [ ] Support ``.. image::`` and ``.. figure::`` directives

.. _todo-scroll-sync:

Synchronized Scrolling
======================================================================

- [ ] Track cursor position in editor
- [ ] Map editor lines to preview positions
- [ ] Sync scroll when editor scrolls
- [ ] Highlight current section in preview

.. _todo-performance:

Performance Improvements
======================================================================

- [ ] Debounce preview updates (currently 50ms)
- [ ] Cache rendered HTML for unchanged content
- [ ] Incremental DOM updates instead of full refresh
- [ ] Background processing for large files

.. _todo-low-priority:

**********************************************************************
Low Priority
**********************************************************************

.. _todo-export:

Export Options
======================================================================

- [ ] Export to HTML file
- [ ] Export to PDF (via browser print)
- [ ] Copy rendered HTML to clipboard

.. _todo-settings:

Enhanced Settings
======================================================================

- [ ] Custom CSS injection option
- [ ] Font size adjustment
- [ ] Line height configuration
- [ ] Code block theme selection

.. _todo-editor:

Editor Enhancements
======================================================================

- [ ] Quick preview refresh action
- [ ] Toggle preview visibility shortcut
- [ ] Zoom controls for preview

.. _todo-completed:

**********************************************************************
Completed
**********************************************************************

.. _todo-completed-1.0.3:

Version 1.0.3
======================================================================

- [x] Fixed all deprecated API usages
- [x] Fixed non-extendable API violation
- [x] Updated Gradle wrapper to 8.13
- [x] Updated IntelliJ Platform Gradle Plugin to 2.10.5

.. _todo-completed-1.0.2:

Version 1.0.2
======================================================================

- [x] Added plugin icon (light and dark variants)

.. _todo-completed-1.0.1:

Version 1.0.1
======================================================================

- [x] JetBrains Marketplace publishing setup
- [x] GitHub Actions CI/CD workflows
- [x] Plugin verification configuration
- [x] Automated release workflow

.. _todo-completed-1.0.0:

Version 1.0.0
======================================================================

- [x] Core split-pane editor implementation
- [x] JCEF-based HTML rendering
- [x] Swing fallback renderer
- [x] Automatic rst2html detection
- [x] pyenv support
- [x] Settings page with path configuration
- [x] Dark/light theme CSS
- [x] External link handling
- [x] Image path resolution
- [x] Build system with Gradle
- [x] Plugin packaging

.. _todo-known-issues:

**********************************************************************
Known Issues
**********************************************************************

.. _todo-issues-pyenv:

pyenv Shim Issues
======================================================================

**Issue**: pyenv shims may not work if no global Python version is set.

**Workaround**: Set a global Python version:

.. code-block:: bash

   pyenv global 3.13.0

Or configure the direct path in settings:

::

   ~/.pyenv/versions/3.13.0/bin/rst2html

.. _todo-issues-jcef:

JCEF Availability
======================================================================

**Issue**: Some platforms/configurations don't support JCEF.

**Status**: Plugin automatically falls back to Swing renderer.

**Impact**: Reduced CSS support with Swing renderer.

.. _todo-ideas:

**********************************************************************
Ideas for Future Versions
**********************************************************************

- Pandoc backend as alternative to docutils
- Live collaboration preview
- Markdown-to-RST conversion
- RST-to-Markdown conversion
- Integration with Sphinx build system
- Preview templates/themes
- TOC sidebar in preview
- Search within preview
