export interface Cookbook {
  title: string;
  recipes: Recipe[];
  tags: string[];
  pageNumbers: number[];
}

export interface Pantry {
  stock: Record<string, number>;
  prices: Record<string, number>;
}

export interface CookingRange {
  temperatureRange: [number, number];
  timeRange: [number, number];
}
