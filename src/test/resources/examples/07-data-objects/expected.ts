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
