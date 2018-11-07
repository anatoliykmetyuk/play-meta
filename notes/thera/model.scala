---
template: default
subpackage: model
---

case class #{modelName}(
  #{aligned f fields ":="}
  #{f.name}: #{f.type}
  #{end aligned}
)
