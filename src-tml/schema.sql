---
[name, fields]
---

CREATE TABLE "$name" (
  ${outdent: 4, ${foreachSep: $fields, \, , ${field =>
    ${opt: field.name, $field}, ${opt: field.type, varchar}

    ${field.name}, ${field.type} ${if: field.unique, \
      CONSTRAINT ${name}_un_${field.name} UNIQUE (${field.name}),\ }
    }}}
)
