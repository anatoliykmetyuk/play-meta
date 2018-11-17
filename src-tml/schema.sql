---
[name, fields]
---

CREATE TABLE "$name" (
  ${foreachSep: $fields, \n\, , ${field =>
    ${opt: field.name, $field}, ${opt: field.type, varchar}\s
  }}
)
