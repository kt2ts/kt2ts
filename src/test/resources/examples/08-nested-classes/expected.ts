export interface Kitchen {
  name: string;
  equipment: Kitchen$Appliance[];
}

export interface Kitchen$Appliance {
  name: string;
  wattage: number;
}
