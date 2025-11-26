# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.1.0] - 2025-11-26

### Added

- Extended MaxsMessageType with indication of debug messages

### Changed

- Prohibited direct access to the KernelNotifications field, provided getter methods instead
- Renamed StandardRoutine for MaxsLoggableRoutine to better reflect its purpose
- Renamed MessageType to MaxMessageType for clarity

### Removed

- Dropped RexsPart class, replaced by direct input if component/part IDs

## [1.0.0] - 2025-09-22

### Added

- First release
