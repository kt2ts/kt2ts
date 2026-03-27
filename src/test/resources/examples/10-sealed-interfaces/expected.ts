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
