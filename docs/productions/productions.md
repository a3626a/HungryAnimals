# Productions

## Introduction

"Production" is a representation of producing items. For example, laying eggs, milking can be interpreted as "Production". So, you can add various productions using this config.

## Grammar

{% code title="productions/{modid}/{anima}l.json" %}
```text
[
  production,
  production,
  ...
]
```
{% endcode %}

{% code title="production " %}
```text
milk
|
egg
|
shear
|
fluid
```
{% endcode %}

{% code title="milk " %}
```text
{
  "type" : "milk",
  "name" : string,
  "delay" : range,
  "condition" : condition,
  "disable_sound" : boolean,
  "input" : itemstack,
  "output" : itemstack
}
```
{% endcode %}

{% code title="egg " %}
```text
{
  "type" : "egg",
  "name" : string,
  "delay" : range,
  "condition" : condition,
  "disable_sound" : boolean,
  "output" : itemstack,
}
```
{% endcode %}

{% code title="shear " %}
```text
{
  "type" : "shear",
  "name" : string,
  "delay" : range,
  "condition" : condition,
  "disable_sound" : boolean,
  "damage" : integer,
  "input" : itemstack,
  "output" : itemstack
}
```
{% endcode %}

{% code title="fluid " %}
```text
{
  "type" : "fluid",
  "name" : string,
  "condition" : condition,
  "fluid" : string,
  "capacity" : integer,
  "amount" : double,
  "weight" : double
}
```
{% endcode %}

{% code title="range " %}
```text
integer
|
{
  "min" : integer,
  "max" : integer
}
```
{% endcode %}

{% code title="condition " %}
```text
{
  "age" : "baby" | "adult",
  "sex" : "female" | "male",
    }
```
{% endcode %}

{% hint style="info" %}
For _itemstack_, follow this page

{% page-ref page="../basics/miscellaneous.md" %}
{% endhint %}

## Usage

A type of production like milk, egg, or shear is distinguished by `"type"` field. Every production must have `"name"` which is used to save data to disc. This `"name"` must be unique. `"condition"`is a requirement for the production. condition has two fields which can be omitted. If `"sex"` is omitted, then animals can produce regardless of sex.

milk production is way of producing milk like Cow. You can specify "bucket" as `"input"`, "milk" as `"output"`. Additionally, you can also set `"delay"`, `"disable_sound"`.  
\* `"input"`/`"output"` don't have to be buckets, you can specify anything.

egg production is way of producing egg like Chicken. You can specify "egg" as `"output"`. Additionally, you can also set `"delay"`, `"disable_sound"`.  
\* There's no limitation on `"output"`, you can specify anything.

shear production is way of producing wool like Sheep. You can specify "wool" as `"output"`, "shears" as `"input"`. `"damage"`determines durability cost per shearing.  
\* There's no limitation on `"output"`, you can specify anything. For `"input"`, it must be damagable, like tools or equipments.

fluid production is way of producing any fluid. You can specify `"fluid"` to indicate what to produce. Without any mod, `"water"`, `"lava"` and `"milk"` are possible. `"capacity"` is an amount of fluid in mB\(milli-bucket\) that an animal can store maximally. An animal produces `"hungryanimals.fluid_amount" * "amount"` mB of the fluid every second. `"hungryanimals.fluid_amount"` is an attribute. An animal consumes `"hungryanimals.fluid_weight" * "weight"`kg of its weight every time it produces 1000mB of the fluid.

range determines delay or cooldown of productions. range with one integer value always has fixed cooldown in integer seconds. range with `"min"` and `"max"` field has random cooldown between `"min"` and `"max"` seconds.

## Example

```text
    {
        "type" : "shear",
        "name" : "minecraft.llama.shear",
        "delay" : {
            "min" : 15,
            "max" : 30
        },
        "condition" : {
            "age" : "adult"
        }
        "disable_sound" : false,
        "damage" : 2,
        "input" : {
            "item" : "minecraft:shears"
        },
        "output" : {
            "item" : "minecraft:string"
        }
    }
```

This production above, produces string from llama by using shears.

