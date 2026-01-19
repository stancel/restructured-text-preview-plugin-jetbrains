# CLAUDE.md - Project Intelligence for RST Preview Plugin

## Project Overview

This is the **ReStructuredText Preview (Standalone)** plugin for JetBrains IDEs.
It provides live HTML preview for `.rst` files without requiring the Python plugin.

**Key Technology:** Uses system `rst2html` (from docutils) to render RST to HTML,
displayed in-IDE using JCEF (embedded Chromium).

---

## Quick Reference

### Build Commands

```bash
# Build the plugin
./gradlew build

# Create distributable ZIP
./gradlew buildPlugin

# Run IDE with plugin for testing
./gradlew runIde

# Clean build artifacts
./gradlew clean
```

### Key Paths

| Path | Purpose |
|------|---------|
| `src/main/java/` | Java source code |
| `src/main/resources/META-INF/plugin.xml` | Plugin configuration |
| `build/distributions/` | Built plugin ZIP |
| `docs/` | RST documentation |

---

## Documentation Standards

### File Format

All documentation should be written in **ReStructuredText (.rst)** format
compatible with Sphinx documentation.

### Section Hierarchy

Use the following characters for section headings, with the special characters
extending to column 70:

1. `#` with overline - for **parts** (top-level divisions)
2. `*` with overline - for **chapters**
3. `=` - for **sections**
4. `-` - for **subsections**
5. `^` - for **subsubsections**
6. `"` - for **paragraphs**

### Section Labels

Every section should have a label for cross-referencing:

```rst
.. _section-label-name:

Section Title
======================================================================
```

Reference with: `:ref:`section-label-name`` or `:ref:`Custom Text <section-label-name>``

### Example Section Header

```rst
.. _example-section:

Example Section Title
======================================================================

Content here...
```

### Linting

Always lint RST files after creation or modification:

```bash
rstcheck filename.rst
# Or for all RST files:
rstcheck *.rst docs/*.rst
```

### Documentation Files

- `README.rst` - Project overview and quick start
- `CHANGELOG.rst` - Version history following Keep a Changelog format
- `TODO.rst` - Planned improvements and tasks
- `docs/*.rst` - Detailed documentation on specific topics

---

## CRITICAL: Git Workflow

**Always follow the Verify → Document → Commit workflow.**

### 1. VERIFY

Before any commit, ensure:

- [ ] Plugin builds successfully: `./gradlew build`
- [ ] Plugin can be installed in target IDE
- [ ] Preview functionality works with test `.rst` files
- [ ] Settings page accessible and functional
- [ ] Lint any RST files: `rstcheck *.rst docs/*.rst`

### 2. DOCUMENT

Update documentation **before** committing:

- [ ] **CHANGELOG.rst** - Add entry for all changes (use proper RST formatting)
- [ ] **TODO.rst** - Update completed items, add new items as needed
- [ ] **README.rst** - Update if setup, architecture, or key info changed
- [ ] **docs/*.rst** - Update relevant detailed documentation

### 3. COMMIT

Only after verification and documentation.

### Commit Message Format

Use descriptive commit messages that explain WHAT changed and WHY:

```text
type: Brief description of change

- Bullet points with specific changes
- Include file names when relevant
- Mention any breaking changes

```

**Type prefixes:**

- `feat:` - New feature or functionality
- `fix:` - Bug fix
- `docs:` - Documentation only
- `refactor:` - Code restructuring
- `chore:` - Maintenance tasks
- `build:` - Build system or dependencies

---

## Architecture Overview

### Core Components

```
RstPreviewEditorProvider
    └── Creates TextEditorWithPreview
            ├── TextEditor (left pane - RST source)
            └── RstPreviewFileEditor (right pane - HTML preview)
                    └── RstPreviewPanel (interface)
                            ├── RstJcefPreviewPanel (JCEF/Chromium)
                            └── RstSwingPreviewPanel (fallback)

RstPreviewProvider
    └── Calls system rst2html via CapturingProcessHandler
    └── Returns HTML + error messages

RstPreviewSettings
    └── Persists rst2html path and panel type
    └── Auto-detects rst2html from pyenv, PATH, common locations
```

### Key Files

| File | Purpose |
|------|---------|
| `RstPreviewEditorProvider.java` | Creates split editor for .rst files |
| `RstPreviewFileEditor.java` | Manages preview pane, listens for document changes |
| `RstPreviewProvider.java` | Converts RST → HTML using system rst2html |
| `RstJcefPreviewPanel.java` | JCEF-based HTML rendering with CSS |
| `RstSwingPreviewPanel.java` | Swing fallback renderer |
| `RstPreviewSettings.java` | Persistent settings, rst2html detection |
| `RstPreviewConfigurable.java` | Settings UI in IDE preferences |
| `plugin.xml` | Plugin registration and extension points |

---

## Development Setup

### Prerequisites

- JDK 21+
- IntelliJ IDEA (for development)
- docutils installed: `pip install docutils`

### First Time Setup

```bash
# Clone the repository
git clone <repo-url>
cd rst-preview-jetbrains

# Build the plugin
./gradlew build

# Run IDE with plugin loaded
./gradlew runIde
```

### Testing Changes

1. Make code changes
2. Run `./gradlew runIde` to launch test IDE
3. Open a `.rst` file to test preview
4. Check Settings → Languages & Frameworks → ReStructuredText Preview

---

## Publishing to JetBrains Marketplace

### Manual Publishing

1. Build: `./gradlew buildPlugin`
2. Go to https://plugins.jetbrains.com/
3. Sign in with JetBrains account
4. Upload `build/distributions/rst-preview-plugin-X.X.X.zip`

### Automated Publishing (GitHub Actions)

See `.github/workflows/publish.yml` for automated releases.

Required secrets:
- `PUBLISH_TOKEN` - JetBrains Marketplace token

---

## Common Issues

### "rst2html not found"

The plugin auto-detects rst2html from:
1. PATH environment variable
2. `~/.pyenv/shims/rst2html`
3. `~/.pyenv/versions/*/bin/rst2html`
4. `~/.local/bin/rst2html`
5. `/usr/bin/rst2html`, `/usr/local/bin/rst2html`

**Fix:** Install docutils or configure path in settings:
```bash
pip install docutils
# Or with pyenv:
pyenv global 3.13.0  # or your version with docutils
```

### Preview shows blank/error

Check:
1. rst2html works from terminal: `echo "Test" | rst2html`
2. Correct Python version active: `pyenv version`
3. Settings path is correct

### JCEF not available

Some platforms don't support JCEF. The plugin automatically falls back to
Swing-based rendering (with reduced CSS support).

---

## Version Compatibility

| Plugin Version | IDE Build Range | Notes |
|----------------|-----------------|-------|
| 1.0.0 | 243 - 253.* | Initial release |

---

## License

Apache License 2.0 - See LICENSE file.
