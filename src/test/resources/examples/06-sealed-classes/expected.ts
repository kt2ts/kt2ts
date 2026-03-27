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
