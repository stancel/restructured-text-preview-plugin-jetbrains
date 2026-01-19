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

.. _todo-marketplace-publishing:

JetBrains Marketplace Publishing
======================================================================

- [ ] Create JetBrains Marketplace vendor account
- [ ] Configure plugin signing
- [ ] Submit initial plugin version for review
- [ ] Set up automated publishing via GitHub Actions

.. _todo-github-actions:

GitHub Actions CI/CD
======================================================================

- [ ] Create build workflow for PRs
- [ ] Create release workflow for tags
- [ ] Add ``PUBLISH_TOKEN`` secret for marketplace
- [ ] Test automated build and publish pipeline

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

.. _todo-issues-deprecated:

Deprecated API Usage
======================================================================

**Issue**: Using deprecated ``UIUtil.isUnderDarcula()`` method.

**Status**: Works but may be removed in future IDE versions.

**Plan**: Update to use ``JBColor.isBright()`` or theme detection APIs.

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
