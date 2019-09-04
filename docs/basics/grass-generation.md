# Grass Generation

## Introduction

`generators.json` contains a list of 'Generator' which generates grass in the world periodically.

## Grammar

{% code-tabs %}
{% code-tabs-item title="generators.json" %}
```text
[
  biome_and_generator,
  biome_and_generator,
  ...
]
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% code-tabs %}
{% code-tabs-item title="biome\_and\_generator " %}
```text
{
  "types" : [biome_type, ...],
  "generator" : generator
}
|
{
  "biome" : string,
  "generator" : generator
}
|
{
  "generator" : generator
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% code-tabs %}
{% code-tabs-item title="biome\_type " %}
```text
"HOT" |
"COLD" |
"SPARSE" |
"DENSE" |
"WET" |
"DRY" |
"SAVANNA" |
"CONIFEROUS" |
"JUNGLE" |
"SPOOKY" |
"DEAD" |
"LUSH" |
"NETHER" |
"END" |
"MUSHROOM" |
"MAGICAL" |
"RARE" |
"OCEAN" |
"RIVER" |
"WATER" |
"MESA" |
"FOREST" |
"PLAINS" |
"MOUNTAIN" |
"HILLS" |
"SWAMP" |
"SANDY" |
"SNOWY" |
"WASTELAND" |
"BEACH" |
"VOID"
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% code-tabs %}
{% code-tabs-item title="generator " %}
```text
{
  "condition" : condition,
  "grass" : block_state | [block_state, ...]
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% code-tabs %}
{% code-tabs-item title="condition " %}
```text
{
  "below" : block_state | [block_state, ...],
  "chance" : double,
  "not_adjacent" : block_state | [block_state, ...]
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% hint style="info" %}
For _block state_, follow this page

{% page-ref page="miscellaneous.md" %}
{% endhint %}

## Usage

There are 3 variants of biome\_and\_generator. First variant which has `"types"` field, applies corresponding generator to every biome wihch has all `"types"`. Second variant which has `"biome"` field, applies corresponding generator to the `"biome"` only. Last one applies the generator to every biome without any other generator. In other words, this is a default generator.

generator has two fields `"condition"` and `"grass"`. When `"condition"` meets, generates `"grass"`. `"grass"` can be a single block\_state or a list of block\_states. When `"grass"` is given as a list, one block\_state is randomly selected and generated.

condition has three fields, and each field can be ommited. Each field represents a condition, and every field must be satisfied. Ommited field is simply considered satisfied. `"below"` is met when block below target position is one of listed block\_state.`"chance"` is met purely by chance. `"1.0"` means always, `"0.0"`means never. Using this field you can specify speed or frequency of grass growth. `"not_adjacent"` is met when there's no block\_state listed nearby target position \(8 blocks\).

## Examples

```text
{
    "biome" : "minecraft:jungle",
    "generator" : {
        "condition" : {
            "below" : {"name" : "minecraft:grass"},
            "chance" : 0.25,
            "not_adjacent" : {
                "name": "minecraft:tallgrass",
                "type": "fern"
            }
        },
        "grass" : {
            "name": "minecraft:tallgrass",
            "type": "fern"
        }
    }
}
```

Grass grows rapidly in jungle.

```text
{
    "biome" : "minecraft:plains",
    "generator" : {
        "condition" : {
            "below" : {"name" : "minecraft:grass"},
            "chance" : 0.1,
            "not_adjacent" : {"name": "minecraft:yellow_flower"}
        },
        "grass" : {
            "name": "minecraft:yellow_flower",
            "type": "dandelion"
        }
    }
}
```

Dandelion is growing in plains.

```text
{
    "types" : ["COLD", "MOUNTAIN"],
    "generator" : {
        "condition" : {
            "below" : {"name" : "minecraft:grass"},
            "chance" : 0.05,
            "not_adjacent" : {"name": "minecraft:tallgrass"}
        },
        "grass" : {
            "name": "minecraft:tallgrass",
            "type": "fern"
        }
    }
}
```

Grass grows very slowly in every "COLD" "MOUNTAIN".

