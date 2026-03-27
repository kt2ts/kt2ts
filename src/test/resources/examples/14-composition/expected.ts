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
