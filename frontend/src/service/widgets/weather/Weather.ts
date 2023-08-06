export interface Weather {
  location: string;
  tempCurrent: number;
  rainProbabilityCurrent: number;
  humidityCurrent: number;
  windSpeedCurrent: number;
  iconCurrent: number;
  iconTextCurrent: string;
  tempMaxToday: number;
  tempMinToday: number;
  tempNextSixHours: Array<number>;
  iconNextSixHours: Array<number>;
  tempMinNextSixDays: Array<number>;
  tempMaxNextSixDays: Array<number>;
  iconNextSixDays: Array<number>;
}
