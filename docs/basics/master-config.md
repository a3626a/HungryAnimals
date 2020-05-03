# Master Config

## Introduction

Master config is an json config file which modifies other config settings. Master config is very useful to modify attributes, ais, and food preferences. It can be easily applied and removed.

Easy config is implemented using master config. `master/master.json` selects master json file to apply according to its field values. There are master config files inside `master/difficulty` and `master/tempo`.

`master/master.json` also provides `custom` field. `modifier`s inside this field are also applied along with easy configs like `difficulty` and `tempo`.

## Grammar

{% code title="master/master.json" %}
```text
{
  "difficulty" : "easy" | "normal" | "hard",
  "tempo" : "fast" | "normal" | "slow",
  "custom" : [
    modifier,
    modifier,
    ...
  ]
}
```
{% endcode %}

{% code title="master/{difficulty\|tempo}/\*.json" %}
```text
[
  modifier,
  modifier,
  ...
]
```
{% endcode %}

{% code title="modifier " %}
```text
{
  "domain" : "default" | "custom" | "all",
  "pattern" : string,
  "modifier" : json object,
}
```
{% endcode %}

{% code title="operator" %}
```text
{
  "operation" : "=" | "*" | "+",
  "value" : double
} 
```
{% endcode %}

## Usage

`"domain"` determines where this modifier is applied. `"default"` means built-in custom config which is generated inside `"config/hungryanimals_example"`, `"custom"` means user created custom config. `"all"` means both.

`"pattern"` determines config files to modify. `*` is wild-card character.

`"modifier"` selects values inside json and modifies them. modifier recursively traverses config files, until it gets operator.

## Example

```text
{
    "domain": "default",
    "pattern": "attributes/minecraft/*.json",
    "modifier": {
        "generic.maxHealth": {
            "operation": "+",
            "value": 10
        }
    }
}
```

This modifier above will increase max health of every animals 5 hearts \(10 points\).

```text
{
	...,
	"generic.maxHealth" : 30,
	...
}
```

This is a part of `"attributes/minecraft/cow.json"`. `"modifier"` field of above example is created by substituting `30` by operator. Additionally, if you want to modify multiple fields at the same time, you can simply substitute fields of target json file like the example following.

```text
{
    "generic.maxHealth": {
        "operation": "+",
        "value": 10
    },
    "hungryanimals.hunger_weight_normal": {
        "operation": "*",
        "value": 1.2
    }
}
```

```text
{
    "domain": "default",
    "pattern": "food_preferences/block/minecraft/*.json",
    "modifier": [
        {
            "nutrient": {"operation": "*","value": 1.3},
            "stomach": {"operation": "*","value": 1.3}
        }
    ]
}
```

This modifier above will increase `"nutrient"` and `"stomach"` of all block-like foods 30%. When the `"modifier"`contains an json array, it will be applied to every elements of the array.

