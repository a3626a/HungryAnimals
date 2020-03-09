# Changing Mob Drops

With Hungry Animals, cows drop more than **30+ meats** by default. It is because HA cows are considered more expensive than vanilla cows. They have more hp, eat a lot, and grow slowly.

There are some methods to change this behavior. First one is **Easy Config**. Next is **Master Config**. The last one is **changing loot tables**.

## Easy Config

{% page-ref page="../basics/master-config.md" %}

You can easily make it`"tempo": "fast"` . This config make animals less expensive, make cows drop **10+ meats**, pigs drop 3 meats.

{% code title="master/master.json" %}
```text
{
  "difficuly": "normal",
  "tempo": "fast",
  "custom": []
}
```
{% endcode %}

## Master Config

You can also precisely control meat drop using Master Config. It is done by changing loot tables.

### Pattern

First of all, you must indicate `pattern` for loot tables. `"loot_tables/minecraft/*.json"` will change all vanilla animals' drop.

{% code title="master/master.json" %}
```text
{
  "difficuly": "normal",
  "tempo": "slow",
  "custom": [
    {
      "domain": "default",
      "pattern": "loot_tables/minecraft/*.json",
      "modifier": modifier
    }
  ]
}
```
{% endcode %}

### Modifier

Next step is building `modifier`. `modifier`s search json objects by matching their shape, and apply operator to found json objects' fields.

`modifier` like _Example A_ searches json files with the `pattern`. Then they will find json object `{}`, then `"height"` field. Value of `"height"` field will be changed by operator.

{% code title="Example A" %}
```text
{
  "domain": "default",
  "pattern": "loot_tables/minecraft/*.json",
  "modifier": {
    "height": operator
  }
}
```
{% endcode %}

Let's checkout examples below. _Target A_ will be modified, but _Target B_ won't. Because _Target B_'s `"height"` is not directly under the base json object, it is wrapped by one more json object, `"image"`.

{% code title="Target A" %}
```text
{
  "height": 480,
  "width": 640,
  "url": "https://a3626a.gitbook.io/hungryanimals"
}
```
{% endcode %}

{% code title="Target B" %}
```text
{
  "image": {
    "height": 360,
    "width": 360,
    "image": "https://a3626a.gitbook.io/hungryanimals"
  },
  "name": "Hungry Animals"
}
```
{% endcode %}

To modify _Target B_, the `modifier` of _Example A_ should contain `"image"`. 

{% code title="Example B" %}
```text
{
  "domain": "default",
  "pattern": "loot_tables/minecraft/*.json",
  "modifier": {
    "image": {
      "height": operator
    }
  }
}
```
{% endcode %}

`modifier`s can contain json array. This modifier searches json array `[]`, and applies operator to every elements of the array.

```text
{
  "domain": "default",
  "pattern": "loot_tables/minecraft/*.json",
  "modifier": [
    operator
  ]
}
```

Finally `modifier` for loot tables looks like _Example C_.

{% code title="Example C" %}
```text
{
  "domain": "default",
  "pattern": "loot_tables/minecraft/*.json",
  "modifier": {
    "pools": [
      {
        "entries": [
          {
            "functions": [
              {
                "weight_per_meat": operator
              }
            ]
          }
        ]
      }
    ]
  }
}
```
{% endcode %}

This will change `"weight_per_meat"` value. `"weight_per_meat"` determines number of meats dropped by animals. The amount is calculated by formula:

$$meat = (weight-0.5*standard\_weight)/weight\_per\_meat$$

For example, 550 kg cow with `weight_per_meat = 10kg` drops `(550kg-0.5*500kg)/(10kg) = 30` meats.

### Operator

`operator`s are one of addition, multiplication, and assignment. _Example Addition_ will add 10 to every searched json elements, _Example Multiplication_ will multiply 4, _Example Assignment_ will set 0.

{% code title="Example Addition" %}
```text
{
  "operation": "+",
  "value": 10
}
```
{% endcode %}

{% code title="Example Multiplication" %}
```text
{
  "operation": "*",
  "value": 4
}
```
{% endcode %}

{% code title="Example Assignment" %}
```text
{
  "operation": "=",
  "value": 0
}
```
{% endcode %}

According to the meat formula above, increasing `"weight_per_meat"` decreases meat dropped. `* 2` will approximately half the amount. Here's the final example, _Example D_.

{% code title="Example D" %}
```text
{
  "domain": "default",
  "pattern": "loot_tables/minecraft/*.json",
  "modifier": {
    "pools": [
      {
        "entries": [
          {
            "functions": [
              {
                "weight_per_meat": {
                  "operation": "*",
                  "value": 4
                }
              }
            ]
          }
        ]
      }
    ]
  }
}
```
{% endcode %}





