export type RecipeId = NominalString<'RecipeId'>
export type IngredientId = NominalString<'IngredientId'>
export type ChefId = NominalString<'ChefId'>

export interface RecipeCard {
  id: RecipeId;
  chefId: ChefId;
  title: string;
  ingredients: IngredientId[];
}
