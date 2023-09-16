<p align="center">
    <img width="300" src="sme-ui-view/src/main/resources/com/malte3d/suturo/sme/ui/view/images/semantic-map-editor-schriftzug-512.png">
</p>
<div align="center">

[![Build and Test](https://github.com/SUTURO/semantic-map-editor/actions/workflows/build-and-test.yml/badge.svg?branch=main)](https://github.com/SUTURO/semantic-map-editor/actions/workflows/build-and-test.yml) [![Release (latest SemVer)](https://img.shields.io/github/v/release/SUTURO/semantic-map-editor)](https://github.com/SUTURO/semantic-map-editor/releases)

</div>

The [SUTURO](https://github.com/suturo) semantic map editor is a Java-based tool to create and edit semantic maps.

## Requirements

- Java 17 or higher

## Usage

...

## Contributing

### Recommend Code Editor

We recommend using [IntelliJ IDEA](https://www.jetbrains.com/idea/) as code editor for this project with the following
plugins:

- Lombok

### Getting Started

1. Open the project in IntelliJ IDEA
2. Execute the Run-Configuration `Run Semantic Map Editor`

The project follows the principles of Domain Driven Design (DDD). The project structure is therefor divided into
multiple Gradle modules to follow the hexagonal architecture. For more information about DDD, see the following

- [Strategic Domain-Driven Design](https://vaadin.com/blog/ddd-part-1-strategic-domain-driven-design)
- [Tactical Domain-Driven Desig](https://vaadin.com/blog/ddd-part-2-tactical-domain-driven-design)
- [Hexagonal Architecture](https://vaadin.com/blog/ddd-part-3-domain-driven-design-and-the-hexagonal-architecture)

### Create a Release

To publish a new release, you need to create a new tag with the version number and push it to the repository.
The release is then published automatically by the GitHub Actions [release.yml](.github/workflows/release.yml) workflow.

Example:

```bash
git tag -a 1.3.0 -m "v1.3.0" -m "- Fix bug in URDF export"
git push origin 1.3.0
```

- The tag name should be a valid [SemVer](https://semver.org/spec/v2.0.0.html) version number.
- The tag message head should start with a `v` prefix and then the tag name (e.g. `v1.3.0`).
- The tag message body should contain a short description of the changes since the last release.

## License

The SUTURO semantic map editor is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for
details.

---

<p align="center">
  <a href="https://www.uni-bremen.de/">
    <img height="40" src="https://suturo.github.io/suturo_knowledge/assets/images/uni-bremen-logo-footer.png">
  </a>
  <span>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;</span>
  <a href="https://github.com/suturo">
    <img height="40" src="https://suturo.github.io/suturo_knowledge/assets/images/suturo-logo-footer.png">
  </a>
</p>
