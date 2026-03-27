# kt2ts - Supported Kotlin Features

This document lists all Kotlin language features supported by the converter,
organized by category. Each category maps to a test file in
`src/test/resources/examples/`.

For each feature, the Kotlin input and expected TypeScript output are shown.

---

## 1. Data classes and primitive types

The most basic building block: a data class with scalar fields becomes a
TypeScript interface.

### Supported primitive mappings

| Kotlin       | TypeScript |
|--------------|------------|
| `String`     | `string`   |
| `Boolean`    | `boolean`  |
| `Int`        | `number`   |
| `Long`       | `number`   |
| `Double`     | `number`   |

```kotlin
data class Recipe(
    val title: String,
    val description: String,
    val preparationMinutes: Int,
    val cookingMinutes: Int,
    val servings: Int,
    val rating: Double,
    val vegetarian: Boolean,
)
```

```typescript
export interface Recipe {
  title: string;
  description: string;
  preparationMinutes: number;
  cookingMinutes: number;
  servings: number;
  rating: number;
  vegetarian: boolean;
}
```

**Test file:** `01-data-class-primitives`

---

## 2. Nullable types

Nullable Kotlin fields (`Type?`) become optional TypeScript fields.

```kotlin
data class Ingredient(
    val name: String,
    val quantity: Double?,
    val unit: String?,
    val notes: String?,
    val organic: Boolean,
)
```

```typescript
export interface Ingredient {
  name: string;
  quantity?: number;
  unit?: string;
  notes?: string;
  organic: boolean;
}
```

**Test file:** `02-nullable-types`

---

## 3. Enums

Kotlin enum classes become TypeScript union types of string literals.

```kotlin
enum class MealType {
    Breakfast,
    Lunch,
    Dinner,
    Snack,
    Dessert,
}
```

```typescript
export type MealType =
  | 'Breakfast'
  | 'Lunch'
  | 'Dinner'
  | 'Snack'
  | 'Dessert'
```

Enums used as fields in data classes:

```kotlin
data class MenuItem(
    val name: String,
    val mealType: MealType,
    val price: Double,
)
```

```typescript
export interface MenuItem {
  name: string;
  mealType: MealType;
  price: number;
}
```

**Test file:** `03-enums`

---

## 4. Collections

### List and Set

Both `List<T>` and `Set<T>` map to TypeScript arrays `T[]`.

```kotlin
data class Cookbook(
    val title: String,
    val recipes: List<Recipe>,
    val tags: Set<String>,
    val pageNumbers: List<Int>,
)
```

```typescript
export interface Cookbook {
  title: string;
  recipes: Recipe[];
  tags: string[];
  pageNumbers: number[];
}
```

### Map

`Map<K, V>` maps to `Record<K, V>` (configurable).

```kotlin
data class Pantry(
    val stock: Map<String, Int>,
    val prices: Map<String, Double>,
)
```

```typescript
export interface Pantry {
  stock: Record<string, number>;
  prices: Record<string, number>;
}
```

### Pair

`Pair<A, B>` maps to a TypeScript tuple `[A, B]`.

```kotlin
data class CookingRange(
    val temperatureRange: Pair<Int, Int>,
    val timeRange: Pair<Int, Int>,
)
```

```typescript
export interface CookingRange {
  temperatureRange: [number, number];
  timeRange: [number, number];
}
```

**Test file:** `04-collections`

---

## 5. Nullable collections and nested nullability

Nullable collections and nullable elements inside collections.

```kotlin
data class WeeklyMenu(
    val days: List<String>,
    val meals: List<String?>,
    val specialMenu: List<String>?,
    val optionalIngredients: List<String?>?,
)
```

```typescript
export interface WeeklyMenu {
  days: string[];
  meals: (string | null)[];
  specialMenu?: string[];
  optionalIngredients?: (string | null)[];
}
```

**Test file:** `05-nullable-collections`

---

## 6. Sealed classes

Sealed classes/interfaces become TypeScript discriminated unions.
Each subclass gets an `objectType` discriminator field.

```kotlin
sealed class CookingMethod

data class Baking(
    val temperatureCelsius: Int,
    val durationMinutes: Int,
    val fanAssisted: Boolean,
) : CookingMethod()

data class Frying(
    val oilType: String,
    val temperatureCelsius: Int,
) : CookingMethod()

data class Steaming(
    val durationMinutes: Int,
    val pressureCooker: Boolean,
) : CookingMethod()

data class RawPreparation(
    val marinadeMinutes: Int?,
) : CookingMethod()
```

```typescript
export interface Baking {
  objectType: 'Baking';
  temperatureCelsius: number;
  durationMinutes: number;
  fanAssisted: boolean;
}

export interface Frying {
  objectType: 'Frying';
  oilType: string;
  temperatureCelsius: number;
}

export interface Steaming {
  objectType: 'Steaming';
  durationMinutes: number;
  pressureCooker: boolean;
}

export interface RawPreparation {
  objectType: 'RawPreparation';
  marinadeMinutes?: number;
}

export type CookingMethod =
  | Baking
  | Frying
  | Steaming
  | RawPreparation
```

**Test file:** `06-sealed-classes`

---

## 7. Data objects (singletons in sealed hierarchies)

Singleton objects extending a sealed class produce interfaces with only the
`objectType` discriminator.

```kotlin
sealed class DietaryRestriction

data class Allergy(
    val allergen: String,
    val severity: String,
) : DietaryRestriction()

data object Vegan : DietaryRestriction()

data object GlutenFree : DietaryRestriction()
```

```typescript
export interface Allergy {
  objectType: 'Allergy';
  allergen: string;
  severity: string;
}

export interface Vegan {
  objectType: 'Vegan';
}

export interface GlutenFree {
  objectType: 'GlutenFree';
}

export type DietaryRestriction =
  | Allergy
  | Vegan
  | GlutenFree
```

**Test file:** `07-data-objects`

---

## 8. Nested classes

Nested classes use `Parent$Child` naming in TypeScript.

```kotlin
data class Kitchen(
    val name: String,
    val equipment: List<Kitchen.Appliance>,
) {
    data class Appliance(
        val name: String,
        val wattage: Int,
    )
}
```

```typescript
export interface Kitchen {
  name: string;
  equipment: Kitchen$Appliance[];
}

export interface Kitchen$Appliance {
  name: string;
  wattage: number;
}
```

**Test file:** `08-nested-classes`

---

## 9. Generics

### Generic data classes

```kotlin
data class PortionOf<T>(
    val content: T,
    val quantity: Double,
    val unit: String,
)
```

```typescript
export interface PortionOf<T> {
  content: T;
  quantity: number;
  unit: string;
}
```

### Nested generics in fields

```kotlin
data class RecipeCollection(
    val byCategory: Map<String, List<Recipe>>,
    val pairings: List<Pair<Recipe, Recipe>>,
)
```

```typescript
export interface RecipeCollection {
  byCategory: Record<string, Recipe[]>;
  pairings: [Recipe, Recipe][];
}
```

**Test file:** `09-generics`

---

## 10. Sealed interfaces

Same behavior as sealed classes but using `sealed interface`.

```kotlin
sealed interface Measurement {
    data class Weight(val grams: Double) : Measurement
    data class Volume(val milliliters: Double) : Measurement
    data class Pieces(val count: Int) : Measurement
}
```

```typescript
export interface Measurement$Weight {
  objectType: 'Weight';
  grams: number;
}

export interface Measurement$Volume {
  objectType: 'Volume';
  milliliters: number;
}

export interface Measurement$Pieces {
  objectType: 'Pieces';
  count: number;
}

export type Measurement =
  | Measurement$Weight
  | Measurement$Volume
  | Measurement$Pieces
```

**Test file:** `10-sealed-interfaces`

---

## 11. Nominal / branded types

Classes inheriting from a configured base class are mapped to branded string
types (e.g., `NominalString<'TypeName'>`). Configured via `nominalStringMappings`.

```kotlin
// Base class configured in nominalStringMappings
abstract class CulinaryId(open val rawId: String)

data class RecipeId(override val rawId: String) : CulinaryId(rawId)
data class IngredientId(override val rawId: String) : CulinaryId(rawId)
data class ChefId(override val rawId: String) : CulinaryId(rawId)
```

```typescript
export type RecipeId = NominalString<'RecipeId'>
export type IngredientId = NominalString<'IngredientId'>
export type ChefId = NominalString<'ChefId'>
```

Usage in a data class:

```kotlin
data class RecipeCard(
    val id: RecipeId,
    val chefId: ChefId,
    val title: String,
    val ingredients: List<IngredientId>,
)
```

```typescript
export interface RecipeCard {
  id: RecipeId;
  chefId: ChefId;
  title: string;
  ingredients: IngredientId[];
}
```

**Test file:** `11-nominal-types`

---

## 12. Custom type mappings

External types mapped to TypeScript via a JSON configuration file.
Typical examples: Java time types, UUID.

Configuration (`mappings.json`):
```json
{
  "java.time.LocalDate": "string",
  "java.time.Instant": "string",
  "java.util.UUID": "string"
}
```

```kotlin
import java.time.LocalDate
import java.time.Instant
import java.util.UUID

data class RecipeEvent(
    val id: UUID,
    val createdAt: Instant,
    val scheduledDate: LocalDate,
    val description: String,
)
```

```typescript
export interface RecipeEvent {
  id: string;
  createdAt: string;
  scheduledDate: string;
  description: string;
}
```

**Test file:** `12-custom-mappings`

---

## 13. Interfaces shared across classes

When multiple data classes implement a common interface, the shared fields
come from that interface.

```kotlin
interface Named {
    val name: String
}

data class Chef(
    override val name: String,
    val specialty: String,
) : Named

data class Restaurant(
    override val name: String,
    val michelinStars: Int,
) : Named
```

```typescript
export interface Chef {
  name: string;
  specialty: string;
}

export interface Restaurant {
  name: string;
  michelinStars: number;
}
```

Note: regular interfaces are not themselves exported. Only concrete classes
annotated for generation produce output. The interface fields are flattened
into each implementing class.

**Test file:** `13-interfaces`

---

## 14. Composition — full example

A realistic example combining multiple features.

```kotlin
data class FullRecipe(
    val id: RecipeId,
    val title: String,
    val chef: Chef,
    val mealType: MealType,
    val cookingMethod: CookingMethod,
    val ingredients: List<Ingredient>,
    val tags: Set<String>,
    val nutritionPerServing: Map<String, Double>,
    val relatedRecipes: List<RecipeId>?,
    val servings: Int,
    val vegetarian: Boolean,
)
```

```typescript
export interface FullRecipe {
  id: RecipeId;
  title: string;
  chef: Chef;
  mealType: MealType;
  cookingMethod: CookingMethod;
  ingredients: Ingredient[];
  tags: string[];
  nutritionPerServing: Record<string, number>;
  relatedRecipes?: RecipeId[];
  servings: number;
  vegetarian: boolean;
}
```

**Test file:** `14-composition`

---

## Feature coverage summary

| #  | Category                 | Kotlin constructs                          | Priority |
|----|--------------------------|--------------------------------------------|----------|
| 01 | Data class + primitives  | `data class`, String/Boolean/Int/Long/Double | P0     |
| 02 | Nullable types           | `Type?`                                    | P0       |
| 03 | Enums                    | `enum class`                               | P0       |
| 04 | Collections              | List, Set, Map, Pair                       | P0       |
| 05 | Nullable collections     | `List<T?>`, `List<T>?`, `List<T?>?`        | P0       |
| 06 | Sealed classes           | `sealed class` + data class subtypes       | P0       |
| 07 | Data objects             | `data object` in sealed hierarchy          | P1       |
| 08 | Nested classes           | Inner data classes                         | P1       |
| 09 | Generics                 | Parameterized classes, nested generics     | P1       |
| 10 | Sealed interfaces        | `sealed interface` + subtypes              | P1       |
| 11 | Nominal types            | Branded string types via inheritance       | P1       |
| 12 | Custom mappings          | External JSON type mappings                | P1       |
| 13 | Interfaces               | Shared interface fields flattened          | P2       |
| 14 | Composition              | All features combined                      | P2       |
