# Item

## Introduction

`'food_preferences/item/{modid}/{animal}.json'` contains list of ingredients which provides `"nutrient"` and `"stomach"`. The animal eats fallen the items, is temped by the items, can be fed by the items.

## Grammar

{% code-tabs %}
{% code-tabs-item title="food\_preferences/item/{modid}/{animal}.json" %}
```text
[
  {
    "item": ingredient,
    "nutrient": double,
    "stomach": double
  },
  ...
]
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% hint style="info" %}
For _ingredient_, follow this page

{% page-ref page="../basics/miscellaneous.md" %}
{% endhint %}

