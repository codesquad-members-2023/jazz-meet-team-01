const weekDays = ['일', '월', '화', '수', '목', '금', '토'];

export const getKoreanWeekdayName = (day: number) => {
  return weekDays[day];
};

export const getFormattedYearMonth = (date: Date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const formattedDate = `${year}${month}`;

  return formattedDate;
};

export const getFormattedDate = (date: Date) => {
  const datePart = getFormattedYearMonth(date);
  const day = String(date.getDate()).padStart(2, '0');
  const formattedDate = `${datePart}${day}`;

  return formattedDate;
};

export const getFormattedDateTime = (date: Date) => {
  const datePart = getFormattedDate(date);
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const formattedDateTime = `${datePart}${hours}${minutes}`;

  return formattedDateTime;
};

export const getFormattedTime = (date: Date) => {
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const formattedTime = `${hours}:${minutes}`;

  return formattedTime;
};

export const getFormattedTimeRange = (start: Date, end: Date) => {
  const startTime = getFormattedTime(start);
  const endTime = getFormattedTime(end);

  return `${startTime}-${endTime}`;
};

export const getFirstDay = (year: number, month: number) => {
  return new Date(year, month - 1, 1).getDay();
};

export const getLastDate = (year: number, month: number) => {
  return new Date(year, month, 0).getDate(); // 0일은 지난 달의 마지막 날을 의미합니다.
};

export const getMonthDates = (date: Date) => {
  const currentYear = date.getFullYear();
  const currentMonth = date.getMonth() + 1;

  const lastDate = getLastDate(currentYear, currentMonth);

  const dates = [];

  for (let i = 1; i <= lastDate; i++) {
    dates.push(new Date(currentYear, currentMonth - 1, i));
  }

  return dates;
};

export const isToday = (date: Date) => {
  const today = new Date();

  return (
    date.getFullYear() === today.getFullYear() &&
    date.getMonth() === today.getMonth() &&
    date.getDate() === today.getDate()
  );
};

export const equalDates = (date1: Date, date2: Date) => {
  const year1 = date1.getFullYear();
  const month1 = date1.getMonth();
  const day1 = date1.getDate();

  const year2 = date2.getFullYear();
  const month2 = date2.getMonth();
  const day2 = date2.getDate();

  return year1 === year2 && month1 === month2 && day1 === day2;
};
