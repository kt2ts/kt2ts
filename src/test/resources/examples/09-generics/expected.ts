export interface PortionOf<T> {
  content: T;
  quantity: number;
  unit: string;
}

export interface RecipeCollection {
  byCategory: Record<string, Recipe[]>;
  pairings: [Recipe, Recipe][];
}
