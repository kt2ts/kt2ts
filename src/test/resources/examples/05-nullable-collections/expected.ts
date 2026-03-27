export interface WeeklyMenu {
  days: string[];
  meals: (string | null)[];
  specialMenu?: string[];
  optionalIngredients?: (string | null)[];
}
