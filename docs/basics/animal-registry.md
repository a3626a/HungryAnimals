# Animal Registry

## Introduction

`animal.json` contains list of the names of animals that will be registered to Hungry Animals.

## Grammar

{% code-tabs %}
{% code-tabs-item title="attributes/modid/animal.json" %}
```text
[
  hungry_animal_entry,
  hungry_animal_entry,
  ...
]
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% code-tabs %}
{% code-tabs-item title="hungry\_animal\_entry " %}
```text
string
|
{
  "name" : string,
  "tamable" : boolean,
  "model_growing" : boolean,
  "sexual" : boolean
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

## Usage

if `"tamable"` is false, then taming is disabled for the animals. Default value is true.

if `"model_growing"` is true, then animals's sizes grow depending on their weight. Default value is true.

if `"sexual"` is false, then animals become asexual. Default value is true.

### Example

```text
[
    {
        "name" : "minecraft:cow"
        "tamable" : false,
        "model_growing" : true
    },
    "minecraft:chicken"
]
```

Cow and Chicken are registered, but cow's taming is disabled. "Disabling taming" is to consider cow as tamed always. Read console logs, or use Debug Glass in game to get name of an animal.

