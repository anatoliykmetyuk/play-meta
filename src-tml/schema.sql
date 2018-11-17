---
[name, fields]
---

CREATE TABLE "$name" (
  ${outdent: 0, ${foreachSep: $fields, \,\n  , ${field =>
    ${opt: field.name, $field}, ${opt: field.type, varchar}}}}
)
