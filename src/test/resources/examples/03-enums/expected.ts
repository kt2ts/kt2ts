export type MealType =
  | 'Breakfast'
  | 'Lunch'
  | 'Dinner'
  | 'Snack'
  | 'Dessert'

export interface MenuItem {
  name: string;
  mealType: MealType;
  price: number;
}
